package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import io.dodn.commerce.storage.db.core.SettlementRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class SettlementReader(
    private val settlementRepository: SettlementRepository,
) {
    fun readRecentSalesAmounts(settlementDate: LocalDate, merchantIds: Collection<Long>): Map<Long, BigDecimal> {
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
            .map {
                Settlement(
                    id = it.id,
                    merchantId = it.merchantId,
                    settlementDate = it.settlementDate,
                    originalAmount = it.originalAmount,
                    feeAmount = it.feeAmount,
                    feeRate = it.feeRate,
                    settlementAmount = it.settlementAmount,
                    state = it.state,
                )
            }
    }
}
