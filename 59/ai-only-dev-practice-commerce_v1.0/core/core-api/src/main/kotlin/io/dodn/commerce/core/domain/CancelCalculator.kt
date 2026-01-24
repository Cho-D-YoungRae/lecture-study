package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CancelCalculator(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val paymentRepository: PaymentRepository,
    private val cancelBalanceRepository: CancelBalanceRepository,
    private val couponRepository: CouponRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    fun calculatePartial(orderKey: String, orderItemId: Long, cancelQuantity: Long): CancelCalculateResult {
        val order = orderRepository.findByOrderKeyAndStatus(orderKey, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val orderItems = orderItemRepository.findByOrderId(order.id)
        val targetItem = orderItems.find { it.id == orderItemId }
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val cancelBalance = cancelBalanceRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val totalCanceledAmount = cancelBalance.cancelledPaidAmount
            .add(cancelBalance.cancelledPointAmount)
            .add(cancelBalance.cancelledCouponAmount)

        val cancelAmount = targetItem.unitPrice.multiply(BigDecimal.valueOf(cancelQuantity))

        var remainingToCancel = cancelAmount

        val cancelPaidAmount: BigDecimal
        val cancelCouponAmount: BigDecimal
        val cancelPointAmount: BigDecimal

        val initialOrderAmount = order.totalPrice.subtract(totalCanceledAmount)
        val availablePaidAmount = cancelBalance.cancellablePaidAmount
        val availableCouponAmount = cancelBalance.cancellableCouponAmount
        val availablePointAmount = cancelBalance.cancellablePointAmount

        val minOrderAmount = if (payment.hasAppliedCoupon()) {
            val ownedCoupon = ownedCouponRepository.findById(payment.ownedCouponId).orElse(null)
            val coupon = ownedCoupon?.let { couponRepository.findById(it.couponId).orElse(null) }
            coupon?.minOrderAmount ?: BigDecimal.ZERO
        } else {
            BigDecimal.ZERO
        }

        val isInitiallyBroken = initialOrderAmount <= minOrderAmount
        val willBeBroken = initialOrderAmount.subtract(cancelAmount) < minOrderAmount

        if (isInitiallyBroken || willBeBroken) {
            // Broken 상태(발생 포함) -> Coupon > Paid > Point 순서
            // 1. Coupon
            val canCancelFromCoupon = if (availableCouponAmount > remainingToCancel) remainingToCancel else availableCouponAmount
            cancelCouponAmount = canCancelFromCoupon
            remainingToCancel = remainingToCancel.subtract(cancelCouponAmount)

            // 2. Paid
            val canCancelFromPaid = if (availablePaidAmount > remainingToCancel) remainingToCancel else availablePaidAmount
            cancelPaidAmount = canCancelFromPaid
            remainingToCancel = remainingToCancel.subtract(cancelPaidAmount)

            // 3. Point
            val canCancelFromPoint = if (availablePointAmount > remainingToCancel) remainingToCancel else availablePointAmount
            cancelPointAmount = canCancelFromPoint
        } else {
            // Broken 아님 -> Paid > Point 순서 (Coupon은 건드리지 않음)
            // 1. Paid
            val canCancelFromPaid = if (availablePaidAmount > remainingToCancel) remainingToCancel else availablePaidAmount
            cancelPaidAmount = canCancelFromPaid
            remainingToCancel = remainingToCancel.subtract(cancelPaidAmount)

            // 2. Point
            val canCancelFromPoint = if (availablePointAmount > remainingToCancel) remainingToCancel else availablePointAmount
            cancelPointAmount = canCancelFromPoint

            // Coupon 은 0
            cancelCouponAmount = BigDecimal.ZERO
        }

        return CancelCalculateResult(
            paidAmount = cancelPaidAmount,
            couponAmount = cancelCouponAmount,
            pointAmount = cancelPointAmount,
            isRestoreCoupon = !isInitiallyBroken && willBeBroken,
        )
    }
}
