package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import io.dodn.commerce.storage.db.core.SettlementEntity
import io.dodn.commerce.storage.db.core.SettlementRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class SettlementGenerator(
    private val settlementCalculator: SettlementCalculator,
    private val settlementRepository: SettlementRepository,
) {
    @Transactional
    fun generate(summaries: List<SettlementTarget>, recentSalesAmountMap: Map<Long, BigDecimal>): Int {
        val settlements = summaries.map { summary ->
            val calculated = settlementCalculator.calculate(summary.targetAmount, recentSalesAmountMap[summary.merchantId]!!)
            SettlementEntity(
                merchantId = summary.merchantId,
                settlementDate = summary.settlementDate,
                originalAmount = calculated.originalAmount,
                feeAmount = calculated.feeAmount,
                feeRate = calculated.feeRate,
                settlementAmount = calculated.settlementAmount,
                state = SettlementState.READY,
            )
        }
        settlementRepository.saveAll(settlements)
        return settlements.size
    }
}
