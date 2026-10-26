package io.dodn.commerce.core.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SettlementCalculatorTest {
    private val settlementCalculator = SettlementCalculator()

    @Test
    @DisplayName("최근 매출이 10,000,000 이하인 경우 수수료 10%가 적용된다")
    fun calculateDefaultFee() {
        // given
        val amount = BigDecimal.valueOf(100_000)
        val recentSalesAmount = BigDecimal.valueOf(10_000_000)

        // when
        val result = settlementCalculator.calculate(amount, recentSalesAmount)

        // then
        assertThat(result.feeRate).isEqualByComparingTo(BigDecimal.valueOf(0.1))
        assertThat(result.feeAmount).isEqualByComparingTo(BigDecimal.valueOf(10_000))
        assertThat(result.settlementAmount).isEqualByComparingTo(BigDecimal.valueOf(90_000))
    }

    @Test
    @DisplayName("최근 매출이 10,000_000 초과 100,000,000 이하인 경우 수수료 8%가 적용된다")
    fun calculateOver10MFee() {
        // given
        val amount = BigDecimal.valueOf(100_000)
        val recentSalesAmount = BigDecimal.valueOf(10_000_001)

        // when
        val result = settlementCalculator.calculate(amount, recentSalesAmount)

        // then
        assertThat(result.feeRate).isEqualByComparingTo(BigDecimal.valueOf(0.08))
        assertThat(result.feeAmount).isEqualByComparingTo(BigDecimal.valueOf(8_000))
        assertThat(result.settlementAmount).isEqualByComparingTo(BigDecimal.valueOf(92_000))
    }

    @Test
    @DisplayName("최근 매출이 100,000,000 초과인 경우 수수료 5%가 적용된다")
    fun calculateOver100MFee() {
        // given
        val amount = BigDecimal.valueOf(100_000)
        val recentSalesAmount = BigDecimal.valueOf(100_000_001)

        // when
        val result = settlementCalculator.calculate(amount, recentSalesAmount)

        // then
        assertThat(result.feeRate).isEqualByComparingTo(BigDecimal.valueOf(0.05))
        assertThat(result.feeAmount).isEqualByComparingTo(BigDecimal.valueOf(5_000))
        assertThat(result.settlementAmount).isEqualByComparingTo(BigDecimal.valueOf(95_000))
    }
}
