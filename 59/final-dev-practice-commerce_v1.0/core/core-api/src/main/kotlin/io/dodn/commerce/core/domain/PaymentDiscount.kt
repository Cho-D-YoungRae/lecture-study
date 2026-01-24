package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import java.math.BigDecimal

data class PaymentDiscount(
    private val ownedCoupons: List<OwnedCoupon>,
    private val pointBalance: PointBalance,
    val useOwnedCouponId: Long,
    private val usePointAmount: BigDecimal,
) {
    val couponType: CouponType
    val couponDiscount: BigDecimal
    val couponMinOrderAmount: BigDecimal
    val usePoint: BigDecimal

    init {
        // 쿠폰 할인 계산
        if (useOwnedCouponId > 0) {
            val ownedCoupon = ownedCoupons.firstOrNull { it.id == useOwnedCouponId }
                ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)
            couponType = ownedCoupon.coupon.type
            couponDiscount = ownedCoupon.coupon.discount
            couponMinOrderAmount = ownedCoupon.coupon.minOrderAmount
        } else {
            couponType = CouponType.NONE
            couponDiscount = BigDecimal.ZERO
            couponMinOrderAmount = BigDecimal.ZERO
        }

        // 포인트 사용액 계산
        usePoint = if (usePointAmount > BigDecimal.ZERO) {
            if (usePointAmount > pointBalance.balance) throw CoreException(ErrorType.POINT_EXCEEDS_BALANCE)
            usePointAmount
        } else {
            BigDecimal.ZERO
        }
    }

    fun paidAmount(orderPrice: BigDecimal): BigDecimal {
        if (orderPrice < couponMinOrderAmount) throw CoreException(ErrorType.OWNED_COUPON_MIN_AMOUNT_NOT_REACHED)

        val couponDiscountAmount = when (couponType) {
            CouponType.NONE -> BigDecimal.ZERO
            CouponType.FIXED_AMOUNT -> couponDiscount
            CouponType.PERCENT_RATE -> orderPrice.multiply(couponDiscount)
        }

        val amount = orderPrice - (couponDiscountAmount + usePointAmount)
        if (amount < BigDecimal.ZERO) throw CoreException(ErrorType.PAYMENT_INVALID_AMOUNT)
        return amount
    }

    companion object {
        val EMPTY = PaymentDiscount(
            ownedCoupons = emptyList(),
            pointBalance = PointBalance(-1, BigDecimal.ZERO),
            useOwnedCouponId = -1,
            usePointAmount = BigDecimal.ZERO,
        )
    }
}
