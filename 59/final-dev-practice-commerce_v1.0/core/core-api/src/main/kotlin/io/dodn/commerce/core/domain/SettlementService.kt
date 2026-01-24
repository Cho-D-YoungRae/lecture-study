package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SettlementService(
    private val settlementTargetReader: SettlementTargetReader,
    private val settlementGenerator: SettlementGenerator,
    private val settlementTargetLoader: SettlementTargetLoader,
    private val settlementTransferProcessor: SettlementTransferProcessor,
    private val settlementReader: SettlementReader,
    private val merchantFinder: MerchantFinder,
) {
    fun loadTargets(targetDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        settlementTargetLoader.loadTargets(targetDate, from, to)
    }

    fun generate(targetDate: LocalDate): Int {
        val summaries = settlementTargetReader.readTargets(targetDate)
        val recentSalesAmountMap = settlementReader.readRecentSalesAmounts(targetDate, summaries.map { it.merchantId })
        return settlementGenerator.generate(summaries, recentSalesAmountMap)
    }

    fun transfer(targetDate: LocalDate) {
        val readySettlements = settlementReader.readByState(SettlementState.READY)
        val merchants = merchantFinder.find(readySettlements.map { it.merchantId })
        settlementTransferProcessor.transfer(targetDate, merchants, readySettlements)
    }
}
