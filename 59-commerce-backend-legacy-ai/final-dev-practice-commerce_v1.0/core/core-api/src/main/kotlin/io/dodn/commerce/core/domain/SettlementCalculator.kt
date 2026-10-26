package io.dodn.commerce.core.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class SettlementCalculator {
    companion object {
        private val DEFAULT_FEE_RATE: BigDecimal = BigDecimal.valueOf(0.1)
        private val OVER_10M_FEE_RATE: BigDecimal = BigDecimal.valueOf(0.08)
        private val OVER_100M_FEE_RATE: BigDecimal = BigDecimal.valueOf(0.05)

        private val SALES_THRESHOLD_100M: BigDecimal = BigDecimal.valueOf(100_000_000)
        private val SALES_THRESHOLD_10M: BigDecimal = BigDecimal.valueOf(10_000_000)
    }

    fun calculate(amount: BigDecimal, recentSalesAmount: BigDecimal): SettlementAmount {
        val feeRate = when {
            recentSalesAmount > SALES_THRESHOLD_100M -> OVER_100M_FEE_RATE
            recentSalesAmount > SALES_THRESHOLD_10M -> OVER_10M_FEE_RATE
            else -> DEFAULT_FEE_RATE
        }

        val feeAmount = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP)

        return SettlementAmount(
            originalAmount = amount,
            feeAmount = feeAmount,
            feeRate = feeRate,
            settlementAmount = amount.subtract(feeAmount).setScale(2, RoundingMode.HALF_UP),
        )
    }
}
