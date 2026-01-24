package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CancelType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.CancelEntity
import io.dodn.commerce.storage.db.core.CancelRepository
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryEntity
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class CancelProcessor(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val paymentRepository: PaymentRepository,
    private val ownedCouponUsageManager: OwnedCouponUsageManager,
    private val cancelRepository: CancelRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val pointHandler: PointHandler,
    private val cancelBalanceRepository: CancelBalanceRepository,
) {
    @Transactional
    fun cancel(orderKey: String): Long {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.PAID, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val cancelBalance = cancelBalanceRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        order.canceled()

        if (payment.hasAppliedCoupon()) {
            ownedCouponUsageManager.revertByAmount(payment.ownedCouponId, BigDecimal.ZERO, BigDecimal.ZERO)
        }

        pointHandler.earn(User(payment.userId), PointType.PAYMENT, payment.id, payment.usedPoint)
        pointHandler.deduct(User(payment.userId), PointType.PAYMENT, payment.id, PointAmount.PAYMENT)

        cancelBalance.cancel(
            paidAmount = payment.paidAmount,
            pointAmount = payment.usedPoint,
            couponAmount = payment.couponDiscount,
        )

        val cancel = cancelRepository.save(
            CancelEntity(
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                originAmount = payment.originAmount,
                ownedCouponId = payment.ownedCouponId,
                couponDiscount = payment.couponDiscount,
                usedPoint = payment.usedPoint,
                paidAmount = payment.paidAmount,
                canceledPaidAmount = payment.paidAmount,
                canceledPointAmount = payment.usedPoint,
                canceledCouponAmount = payment.couponDiscount,
                externalCancelKey = "PG_API_응답_취소_고유_값_저장",
                type = CancelType.ALL,
                canceledAt = LocalDateTime.now(),
            ),
        )

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.CANCEL,
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                externalPaymentKey = payment.externalPaymentKey!!,
                paidAmount = payment.paidAmount,
                pointAmount = payment.usedPoint,
                couponAmount = payment.couponDiscount,
                message = "취소 성공",
                occurredAt = cancel.canceledAt,
            ),
        )

        return cancel.id
    }

    @Transactional
    fun partialCancel(
        orderKey: String,
        orderItemId: Long,
        quantity: Long,
        calculateResult: CancelCalculateResult,
    ): Long {
        val order = orderRepository.findByOrderKeyAndStatus(orderKey, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val cancelBalance = cancelBalanceRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val targetItem = orderItemRepository.findByIdOrNull(orderItemId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        // 1. OrderItem 수량 및 상태 업데이트
        targetItem.cancel(quantity)

        // 2. Order 상태 업데이트
        val orderItems = orderItemRepository.findByOrderId(order.id)
        if (orderItems.all { it.isAllCanceled() }) {
            order.canceled()
        } else {
            order.partialCanceled()
        }

        // 3. 쿠폰 복원
        if (calculateResult.isRestoreCoupon) {
            ownedCouponUsageManager.revertByAmount(payment.ownedCouponId, BigDecimal.ZERO, BigDecimal.ZERO)
        }

        // 4. 포인트 환불
        if (calculateResult.pointAmount > BigDecimal.ZERO) {
            pointHandler.earn(User(payment.userId), PointType.PAYMENT, payment.id, calculateResult.pointAmount)
        }

        // 5. 잔액 업데이트
        cancelBalance.cancel(
            paidAmount = calculateResult.paidAmount,
            pointAmount = calculateResult.pointAmount,
            couponAmount = calculateResult.couponAmount,
        )

        // 6. CancelEntity 저장
        val cancel = cancelRepository.save(
            CancelEntity(
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                originAmount = payment.originAmount,
                ownedCouponId = payment.ownedCouponId,
                couponDiscount = calculateResult.couponAmount,
                usedPoint = calculateResult.pointAmount,
                paidAmount = calculateResult.paidAmount,
                canceledPaidAmount = calculateResult.paidAmount,
                canceledPointAmount = calculateResult.pointAmount,
                canceledCouponAmount = calculateResult.couponAmount,
                externalCancelKey = "PG_API_응답_취소_고유_값_저장",
                type = CancelType.PARTIAL,
                canceledAt = LocalDateTime.now(),
            ),
        )

        // 7. TransactionHistory 저장
        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PARTIAL_CANCEL,
                userId = payment.userId,
                orderId = payment.orderId,
                paymentId = payment.id,
                externalPaymentKey = payment.externalPaymentKey!!,
                paidAmount = calculateResult.paidAmount,
                pointAmount = calculateResult.pointAmount,
                couponAmount = calculateResult.couponAmount,
                message = "부분 취소 성공",
                occurredAt = cancel.canceledAt,
            ),
        )

        return cancel.id
    }
}
