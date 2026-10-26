package io.dodn.commerce.core.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.collections.groupBy

@Component
class SettlementTransferProcessor(
    private val settlementTransferHandler: SettlementTransferHandler,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun transfer(targetDate: LocalDate, merchants: List<Merchant>, settlements: List<Settlement>) {
        val merchantMap = merchants.associateBy { it.id }
        val settlementMap = settlements.groupBy { it.merchantId }

        for (settlementGroup in settlementMap) {
            val merchantId = settlementGroup.key
            val merchant = merchantMap[merchantId]
            try {
                if (merchant == null) {
                    log.warn("[SETTLEMENT_TRANSFER] {} 가맹점 정보를 찾을 수 없습니다.", merchantId)
                    continue
                }

                if (targetDate.dayOfMonth % merchant.settlementCycle != 0) {
                    log.info("[SETTLEMENT_TRANSFER] {} 가맹점은 정산 주기가 아닙니다. (cycle: {}, dayOfMonth: {})", merchantId, merchant.settlementCycle, targetDate.dayOfMonth)
                    continue
                }

                val transferAmount = settlementGroup.value.sumOf { it.settlementAmount }
                if (transferAmount <= BigDecimal.ZERO) {
                    log.warn("[SETTLEMENT_TRANSFER] {} 가맹점 미정산 금액 : {} 발생 확인 요망!", merchantId, transferAmount)
                    continue
                }

                /**
                 * NOTE: 외부 펌 등 이체 서비스 API 호출
                 */

                settlementTransferHandler.success(settlementGroup.value)
            } catch (e: Exception) {
                log.error("[SETTLEMENT_TRANSFER] {} 가맹점 정산 중 에러 발생: {}", merchantId, e.message, e)
            }
        }
    }
}
