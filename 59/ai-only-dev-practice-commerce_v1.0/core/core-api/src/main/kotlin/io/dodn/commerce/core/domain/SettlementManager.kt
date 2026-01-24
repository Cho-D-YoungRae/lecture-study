package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import io.dodn.commerce.storage.db.core.SettlementEntity
import io.dodn.commerce.storage.db.core.SettlementRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SettlementManager(
    private val settlementRepository: SettlementRepository,
    private val settlementReader: SettlementReader,
) {
    @Transactional
    fun createSettlements(summaries: List<SettlementTargetSummary>): Int {
        val settlementDate = summaries.first().settlementDate
        val merchantIds = summaries.map { it.merchantId }.toSet()
        val recentSalesAmountMap = settlementReader.readRecentSalesAmounts(merchantIds, settlementDate)

        val settlements = summaries.map { summary ->
            val recentSalesAmount = recentSalesAmountMap[summary.merchantId] ?: java.math.BigDecimal.ZERO
            val amount = SettlementCalculator.calculate(summary.targetAmount, recentSalesAmount)
            SettlementEntity(
                merchantId = summary.merchantId,
                settlementDate = summary.settlementDate,
                originalAmount = amount.originalAmount,
                feeAmount = amount.feeAmount,
                feeRate = amount.feeRate,
                settlementAmount = amount.settlementAmount,
                state = SettlementState.READY,
            )
        }
        settlementRepository.saveAll(settlements)
        return settlements.size
    }

    @Transactional
    fun completeTransfer(settlements: List<Settlement>) {
        val entities = settlementRepository.findAllById(settlements.map { it.id })
        entities.forEach { it.sent() }
        settlementRepository.saveAll(entities)
    }
}
