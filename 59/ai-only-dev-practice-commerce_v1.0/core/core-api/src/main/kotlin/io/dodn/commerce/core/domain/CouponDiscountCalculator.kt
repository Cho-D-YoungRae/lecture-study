package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponType
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class CouponDiscountCalculator {
    fun calculateDiscount(orderAmount: BigDecimal, coupon: Coupon): BigDecimal {
        if (orderAmount <= BigDecimal.ZERO) return BigDecimal.ZERO
        // minOrderAmount not satisfied → not usable
        if (coupon.minOrderAmount != null && orderAmount < coupon.minOrderAmount) return BigDecimal.ZERO

        return when (coupon.type) {
            CouponType.FIXED_AMOUNT -> fixedAmount(orderAmount, coupon.discount)
            CouponType.PERCENT_RATE -> percentRate(orderAmount, coupon.discount)
        }
    }

    private fun fixedAmount(orderAmount: BigDecimal, discountAmount: BigDecimal): BigDecimal {
        if (discountAmount <= BigDecimal.ZERO) return BigDecimal.ZERO
        return orderAmount.min(discountAmount).stripTrailingZerosSafe()
    }

    private fun percentRate(orderAmount: BigDecimal, percent: BigDecimal): BigDecimal {
        // Interpret coupon.discount as percentage value (1..100)
        if (percent < BigDecimal.ONE) return BigDecimal.ZERO
        if (percent > BigDecimal(100)) return BigDecimal.ZERO
        val rate = percent.divide(BigDecimal(100))

        val raw = orderAmount.multiply(rate)
        // Default policy: round to currency unit (0 decimal places), HALF_UP
        return raw.setScale(0, RoundingMode.HALF_UP).stripTrailingZerosSafe()
    }
}

private fun BigDecimal.stripTrailingZerosSafe(): BigDecimal = try {
    this.stripTrailingZeros()
} catch (_: ArithmeticException) {
    this
}
