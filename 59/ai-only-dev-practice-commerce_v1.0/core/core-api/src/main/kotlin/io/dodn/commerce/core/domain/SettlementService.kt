package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SettlementService(
    private val settlementReader: SettlementReader,
    private val settlementManager: SettlementManager,
    private val settlementTargetProcessor: SettlementTargetProcessor,
    private val settlementTransferProcessor: SettlementTransferProcessor,
) {
    fun loadTargets(settleDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        settlementTargetProcessor.loadTargets(settleDate, from, to)
    }

    fun calculate(settleDate: LocalDate): Int {
        val summaries = settlementReader.readSummary(settleDate)
        return settlementManager.createSettlements(summaries)
    }

    fun transfer(): Int {
        return settlementTransferProcessor.transfer()
    }
}
