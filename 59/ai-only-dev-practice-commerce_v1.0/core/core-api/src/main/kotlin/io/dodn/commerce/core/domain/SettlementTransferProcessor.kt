package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class SettlementTransferProcessor(
    private val settlementReader: SettlementReader,
    private val settlementManager: SettlementManager,
    private val merchantFinder: MerchantFinder,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun transfer(): Int {
        val now = LocalDate.now()
        val readySettlements = settlementReader.readByState(SettlementState.READY)
            .groupBy { it.merchantId }

        val merchantIds = readySettlements.keys
        val merchants = merchantFinder.findByIds(merchantIds).associateBy { it.id }

        var transferCount = 0
        for (settlementGroup in readySettlements) {
            val merchantId = settlementGroup.key
            val merchant = merchants[merchantId]
            try {
                if (merchant == null) {
                    log.warn("[SETTLEMENT_TRANSFER] {} 가맹점 정보를 찾을 수 없습니다.", merchantId)
                    continue
                }

                if (now.dayOfMonth % merchant.settlementCycle != 0) {
                    log.info("[SETTLEMENT_TRANSFER] {} 가맹점은 정산 주기가 아닙니다. (cycle: {}, dayOfMonth: {})", merchantId, merchant.settlementCycle, now.dayOfMonth)
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

                settlementManager.completeTransfer(settlementGroup.value)
                transferCount++
            } catch (e: Exception) {
                log.error("[SETTLEMENT_TRANSFER] {} 가맹점 정산 중 에러 발생: {}", merchantId, e.message, e)
            }
        }
        return transferCount
    }
}
