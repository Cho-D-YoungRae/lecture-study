package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.SettlementTargetRepository
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SettlementTargetReader(
    private val settlementTargetRepository: SettlementTargetRepository,
) {
    fun readTargets(targetDate: LocalDate): List<SettlementTarget> {
        return settlementTargetRepository.findSummary(targetDate)
            .map {
                SettlementTarget(
                    merchantId = it.merchantId,
                    settlementDate = it.settlementDate,
                    targetAmount = it.targetAmount,
                    targetCount = it.targetCount,
                    orderCount = it.orderCount,
                )
            }
    }
}
