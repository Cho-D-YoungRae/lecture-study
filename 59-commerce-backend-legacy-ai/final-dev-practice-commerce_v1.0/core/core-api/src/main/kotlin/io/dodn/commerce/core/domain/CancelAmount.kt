package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class CancelAmount(
    val cancelAmount: BigDecimal,
    val totalOrderAmount: BigDecimal,
    val totalCanceledAmount: BigDecimal,
    val cancellablePaidAmount: BigDecimal,
    val cancellablePointAmount: BigDecimal,
    val cancellableCouponAmount: BigDecimal,
    val minOrderAmount: BigDecimal,
) {
    fun paidAmount(): BigDecimal {
        return (cancelAmount - couponAmount()).min(cancellablePaidAmount)
    }

    fun couponAmount(): BigDecimal {
        return if (isBrokenCoupon()) {
            cancelAmount.min(cancellableCouponAmount)
        } else {
            BigDecimal.ZERO
        }
    }

    fun pointAmount(): BigDecimal {
        return (cancelAmount - couponAmount() - paidAmount()).min(cancellablePointAmount)
    }

    private fun remainOrderAmount(): BigDecimal {
        return totalOrderAmount - totalCanceledAmount
    }

    fun isRestoreCoupon(): Boolean {
        return remainOrderAmount() > minOrderAmount && isBrokenCoupon()
    }

    private fun isBrokenCoupon(): Boolean {
        return remainOrderAmount() - cancelAmount < minOrderAmount
    }
}
