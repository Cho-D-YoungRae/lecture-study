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
class SettlementTargetLoader(
    private val settlementSourceReader: SettlementSourceReader,
    private val settlementTargetProcessor: SettlementTargetManager,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun loadTargets(targetDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        processPayment(targetDate, from, to)
        processCancel(targetDate, from, to)
    }

    private fun processPayment(targetDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        var paymentPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val payments = settlementSourceReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                paymentPageable,
            )
            try {
                settlementTargetProcessor.processPayments(targetDate, payments.content)
            } catch (e: Exception) {
                log.error("[SETTLEMENT_LOAD_TARGETS] `결제` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}", paymentPageable.offset, paymentPageable.pageSize, paymentPageable.pageNumber, e.message, e)
            }
            paymentPageable = payments.nextPageable()
        } while (payments.hasNext())
    }

    private fun processCancel(targetDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        var cancelPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val cancels = settlementSourceReader.readCancelsByCanceledAtBetween(from, to, cancelPageable)
            try {
                settlementTargetProcessor.processCancels(targetDate, cancels.content)
            } catch (e: Exception) {
                log.error("[SETTLEMENT_LOAD_TARGETS] `취소` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}", cancelPageable.offset, cancelPageable.pageSize, cancelPageable.pageNumber, e.message, e)
            }
            cancelPageable = cancels.nextPageable()
        } while (cancels.hasNext())
    }
}
