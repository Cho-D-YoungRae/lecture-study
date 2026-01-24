package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CancelCalculator(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val paymentRepository: PaymentRepository,
    private val cancelBalanceRepository: CancelBalanceRepository,
    private val ownedCouponReader: OwnedCouponReader,
) {
    fun calculatePartial(action: PartialCancelAction): CancelCalculated {
        val order = orderRepository.findByOrderKeyAndStatus(action.orderKey, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val targetItem = orderItemRepository.findByIdOrNull(action.orderItemId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val cancelBalance = cancelBalanceRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val minOrderAmount = if (payment.hasAppliedCoupon()) {
            ownedCouponReader.getOwnedCoupon(payment.ownedCouponId).coupon.minOrderAmount
        } else {
            BigDecimal.ZERO
        }

        val cancelAmount = CancelAmount(
            cancelAmount = targetItem.unitPrice * action.quantity.toBigDecimal(),
            totalOrderAmount = order.totalPrice,
            totalCanceledAmount = cancelBalance.totalCanceledAmount(),
            cancellablePaidAmount = cancelBalance.cancellablePaidAmount,
            cancellablePointAmount = cancelBalance.cancellablePointAmount,
            cancellableCouponAmount = cancelBalance.cancellableCouponAmount,
            minOrderAmount = minOrderAmount,
        )

        return CancelCalculated(
            paidAmount = cancelAmount.paidAmount(),
            couponAmount = cancelAmount.couponAmount(),
            pointAmount = cancelAmount.pointAmount(),
            isRestoreCoupon = cancelAmount.isRestoreCoupon(),
        )
    }
}
