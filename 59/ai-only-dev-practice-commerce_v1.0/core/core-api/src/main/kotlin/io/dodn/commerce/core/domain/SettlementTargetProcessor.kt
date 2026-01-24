package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class SettlementTargetProcessor(
    private val settlementTargetReader: SettlementTargetReader,
    private val settlementTargetManager: SettlementTargetManager,
    private val merchantFinder: MerchantFinder,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun loadTargets(settleDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        val dayOfMonth = settleDate.dayOfMonth
        val merchants = merchantFinder.findAll()
        val targetMerchantIds = merchants
            .filter { dayOfMonth % it.settlementCycle == 0 }
            .map { it.id }
            .toSet()

        if (targetMerchantIds.isEmpty()) {
            log.info("[SETTLEMENT_LOAD_TARGETS] 정산 대상 가맹점이 없습니다. settleDate: {}", settleDate)
            return
        }

        var paymentPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val payments = settlementTargetReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                paymentPageable,
            )
            try {
                settlementTargetManager.processPayments(settleDate, payments.content, targetMerchantIds)
            } catch (e: Exception) {
                log.error(
                    "[SETTLEMENT_LOAD_TARGETS] `결제` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}",
                    paymentPageable.offset,
                    paymentPageable.pageSize,
                    paymentPageable.pageNumber,
                    e.message,
                    e,
                )
            }
            paymentPageable = payments.nextPageable()
        } while (payments.hasNext())

        var cancelPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val cancels = settlementTargetReader.readCancelsByCanceledAtBetween(from, to, cancelPageable)
            try {
                settlementTargetManager.processCancels(settleDate, cancels.content, targetMerchantIds)
            } catch (e: Exception) {
                log.error(
                    "[SETTLEMENT_LOAD_TARGETS] `취소` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}",
                    cancelPageable.offset,
                    cancelPageable.pageSize,
                    cancelPageable.pageNumber,
                    e.message,
                    e,
                )
            }
            cancelPageable = cancels.nextPageable()
        } while (cancels.hasNext())
    }
}
