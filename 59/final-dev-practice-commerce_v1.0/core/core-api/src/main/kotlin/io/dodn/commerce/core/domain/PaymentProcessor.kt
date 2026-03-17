package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CancelBalanceEntity
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryEntity
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class PaymentProcessor(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val pointHandler: PointHandler,
    private val ownedCouponUsageManager: OwnedCouponUsageManager,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val cancelBalanceRepository: CancelBalanceRepository,
) {
    /**
     * ## 쿠폰 다회권 동시성 처리 이슈
     *
     * 경합이 발생했을 때 낙관적 락으로 처리하고 예외를 던지도록 했다.
     * > 돈이 빠져나간 뒤에 쿠폰을 사용하고 있어서 낙관적 락 충돌 발생해서 예외를 던지면 다 롤백이 될 것
     *
     * 여기서 고민을 해야한다.
     *
     * 쿠폰이 동시성 충돌이 난다는 것은 일반적인 상황이 아니다. -> 어뷰저일 수도 있다.
     *
     * 혹은 가족 계정처럼 아이디를 돌려 쓸 수 있다.
     *
     * 이런 것들은 결국 전략이 된다. 어떻게 서비스를 운영할 것인가 어떻게 기능을 만들 것인가를 고민한다.
     */
    @Transactional
    fun success(orderKey: String, externalPaymentKey: String): Long {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        payment.success(
            externalPaymentKey,
            PaymentMethod.CARD,
            "PG 승인 API 호출의 응답 값 중 `승인번호` 넣기",
        )
        order.paid()

        val orderItems = orderItemRepository.findByOrderId(order.id)
        orderItems.forEach { it.paid() }

        if (payment.hasAppliedCoupon()) {
            ownedCouponUsageManager.use(payment.ownedCouponId)
        }

        pointHandler.deduct(User(payment.userId), PointType.PAYMENT, payment.id, payment.usedPoint)
        pointHandler.earn(User(payment.userId), PointType.PAYMENT, payment.id, PointAmount.PAYMENT)

        cancelBalanceRepository.save(
            CancelBalanceEntity(
                orderId = order.id,
                cancellablePaidAmount = payment.paidAmount,
                cancellablePointAmount = payment.usedPoint,
                cancellableCouponAmount = payment.couponDiscount,
            ),
        )

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT,
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                externalPaymentKey = externalPaymentKey,
                paidAmount = payment.paidAmount,
                pointAmount = payment.usedPoint,
                couponAmount = payment.couponDiscount,
                message = "결제 성공",
                occurredAt = payment.paidAt!!,
            ),
        )
        return payment.id
    }

    @Transactional
    fun fail(orderKey: String, code: String, message: String) {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT_FAIL,
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                externalPaymentKey = "",
                paidAmount = BigDecimal.valueOf(-1),
                pointAmount = BigDecimal.valueOf(-1),
                couponAmount = BigDecimal.valueOf(-1),
                message = "[$code] $message",
                occurredAt = LocalDateTime.now(),
            ),
        )
    }
}
