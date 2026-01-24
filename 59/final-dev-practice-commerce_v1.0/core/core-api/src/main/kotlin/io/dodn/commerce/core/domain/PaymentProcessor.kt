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
