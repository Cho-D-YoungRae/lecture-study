package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryEntity
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val pointHandler: PointHandler,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    @Transactional
    fun createPayment(
        order: Order,
        paymentDiscount: PaymentDiscount,
    ): Long {
        if (paymentRepository.findByOrderId(order.id)?.state == PaymentState.SUCCESS) {
            throw CoreException(ErrorType.ORDER_ALREADY_PAID)
        }

        val payment = PaymentEntity(
            userId = order.userId,
            orderId = order.id,
            originAmount = order.totalPrice,
            ownedCouponId = paymentDiscount.useOwnedCouponId,
            couponDiscount = paymentDiscount.couponDiscount,
            usedPoint = paymentDiscount.usePoint,
            paidAmount = paymentDiscount.paidAmount(order.totalPrice),
            state = PaymentState.READY,
        )
        return paymentRepository.save(payment).id
    }

    @Transactional
    fun success(orderKey: String, externalPaymentKey: String, amount: BigDecimal): Long {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.userId != order.userId) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.state != PaymentState.READY) throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        if (payment.paidAmount != amount) throw CoreException(ErrorType.PAYMENT_AMOUNT_MISMATCH)

        /**
         * NOTE: PG 승인 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         *
         * 이 아래로는 결제가 된 것이기 때문에 가급적 예외가 발생하면 안됨 -> 트랜잭션 처리
         * 어디에 실패하더라도 로깅만 하고 넘어가는 것이 나을 수도 있음
         */

        payment.success(
            externalPaymentKey,
            // NOTE: PG 승인 API 호출의 응답 값 중 `결제 수단` 넣기
            PaymentMethod.CARD,
            "PG 승인 API 호출의 응답 값 중 `승인번호` 넣기",
        )
        order.paid()

        if (payment.hasAppliedCoupon()) {
            ownedCouponRepository.findByIdOrNull(payment.ownedCouponId)?.use()
        }

        pointHandler.deduct(User(payment.userId), PointType.PAYMENT, payment.id, payment.usedPoint)
        pointHandler.earn(User(payment.userId), PointType.PAYMENT, payment.id, PointAmount.PAYMENT)

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT,
                userId = order.userId,
                orderId = order.id,
                paymentId = payment.id,
                externalPaymentKey = externalPaymentKey,
                amount = payment.paidAmount,
                message = "결제 성공",
                occurredAt = payment.paidAt!!,
            ),
        )
        return payment.id
    }

    /**
     * 주문의 상태를 기록하지 않음. 결제에 대한 상태를 기록하지 않음. -> TransactionHistory에 실패 기록만 남김
     * 아래 코드 전략으로 보면
     * * 주문이 결제가 성공될 떄까지 주문을 재사용 가능
     * * 결제는 생성될 수는 있음
     *
     * 기획에서 결제 실패한 것에 대해서 상태를 알고 싶다고 할 수 있음
     * * 결제가 실패된건지, 주문창에서 꺼버린건지 등
     * * 별도 데이터 적재한 걸로 체크할 수 있다면 트랜잭션 히스토리 등을 통해서 결제 실패 상태 등은 알 수 있음
     *
     * 상태 핸들링은 서비스를 운영하는데 귀찮고 복잡하게 만들기 때문에 최적화하는게 운영하고 확장해나가는데 도움이 됨
     * * 상태는 문제를 많이 일으킬 수 있다
     */
    fun fail(orderKey: String, code: String, message: String) {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT_FAIL,
                userId = order.userId,
                orderId = order.id,
                paymentId = payment.id,
                externalPaymentKey = "",
                amount = BigDecimal.valueOf(-1),
                message = "[$code] $message",
                occurredAt = LocalDateTime.now(),
            ),
        )
    }
}
