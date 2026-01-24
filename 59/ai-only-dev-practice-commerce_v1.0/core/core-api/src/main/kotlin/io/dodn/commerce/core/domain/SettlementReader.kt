package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import io.dodn.commerce.storage.db.core.SettlementEntity
import io.dodn.commerce.storage.db.core.SettlementRepository
import io.dodn.commerce.storage.db.core.SettlementTargetRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class SettlementReader(
    private val settlementTargetRepository: SettlementTargetRepository,
    private val settlementRepository: SettlementRepository,
) {
    fun readSummary(settlementDate: LocalDate): List<SettlementTargetSummary> {
        return settlementTargetRepository.findSummary(settlementDate)
            .map {
                SettlementTargetSummary(
                    merchantId = it.merchantId,
                    settlementDate = it.settlementDate,
                    targetAmount = it.targetAmount,
                    targetCount = it.targetCount,
                    orderCount = it.orderCount,
                )
            }
    }

    fun readRecentSalesAmounts(merchantIds: Set<Long>, settlementDate: LocalDate): Map<Long, BigDecimal> {
        val startDate = settlementDate.minusMonths(1)
        val endDate = settlementDate.minusDays(1)
        return settlementRepository.sumOriginalAmountByMerchantIdInAndSettlementDateBetweenAndState(
            merchantIds = merchantIds,
            startDate = startDate,
            endDate = endDate,
            state = SettlementState.SENT,
        ).associate { it.getMerchantId() to it.getAmount() }
    }

    fun readByState(state: SettlementState): List<Settlement> {
        return settlementRepository.findByState(state)
            .map { it.toConcept() }
    }

    private fun SettlementEntity.toConcept(): Settlement {
        return Settlement(
            id = id,
            merchantId = merchantId,
            settlementDate = settlementDate,
            originalAmount = originalAmount,
            feeAmount = feeAmount,
            feeRate = feeRate,
            settlementAmount = settlementAmount,
            state = state,
        )
    }
}
