package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.time.LocalDateTime

class SettlementTargetLoaderTest {
    private val settlementSourceReader: SettlementSourceReader = mock()
    private val settlementTargetManager: SettlementTargetManager = mock()
    private val settlementTargetLoader = SettlementTargetLoader(
        settlementSourceReader,
        settlementTargetManager,
    )

    @Test
    fun `정산 주기에 해당하는 가맹점만 정산 대상으로 적재한다`() {
        // given
        val settleDate = LocalDate.of(2025, 12, 25) // 25일
        val from = LocalDateTime.of(2025, 12, 24, 0, 0)
        val to = LocalDateTime.of(2025, 12, 24, 23, 59, 59)

        val pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        given(
            settlementSourceReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        given(
            settlementSourceReader.readCancelsByCanceledAtBetween(
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        // when
        settlementTargetLoader.loadTargets(settleDate, from, to)

        // then
        then(settlementTargetManager).should().processPayments(settleDate, emptyList())
        then(settlementTargetManager).should().processCancels(settleDate, emptyList())
    }

    @Test
    fun `데이터가 있는 경우 페이지네이션을 따라가며 모두 적재한다`() {
        // given
        val settleDate = LocalDate.of(2025, 12, 25)
        val from = LocalDateTime.of(2025, 12, 24, 0, 0)
        val to = LocalDateTime.of(2025, 12, 24, 23, 59, 59)

        val pageable0 = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        val pageable1 = PageRequest.of(1, 1000, Sort.by(Sort.Direction.ASC, "id"))

        val payment = mock(SettlementPayment::class.java)
        val cancel = mock(SettlementCancel::class.java)

        given(
            settlementSourceReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                pageable0,
            ),
        ).willReturn(SliceImpl(listOf(payment), pageable0, true))

        given(
            settlementSourceReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                pageable1,
            ),
        ).willReturn(SliceImpl(emptyList(), pageable1, false))

        given(
            settlementSourceReader.readCancelsByCanceledAtBetween(
                from,
                to,
                pageable0,
            ),
        ).willReturn(SliceImpl(listOf(cancel), pageable0, true))

        given(
            settlementSourceReader.readCancelsByCanceledAtBetween(
                from,
                to,
                pageable1,
            ),
        ).willReturn(SliceImpl(emptyList(), pageable1, false))

        // when
        settlementTargetLoader.loadTargets(settleDate, from, to)

        // then
        then(settlementTargetManager).should().processPayments(settleDate, listOf(payment))
        then(settlementTargetManager).should().processPayments(settleDate, emptyList())
        then(settlementTargetManager).should().processCancels(settleDate, listOf(cancel))
        then(settlementTargetManager).should().processCancels(settleDate, emptyList())
    }

    @Test
    fun `데이터가 없어도 정상 동작한다`() {
        // given
        val settleDate = LocalDate.of(2025, 12, 24)
        val from = LocalDateTime.of(2025, 12, 23, 0, 0)
        val to = LocalDateTime.of(2025, 12, 23, 23, 59, 59)

        val pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        given(
            settlementSourceReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        given(
            settlementSourceReader.readCancelsByCanceledAtBetween(
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        // when
        settlementTargetLoader.loadTargets(settleDate, from, to)

        // then
        then(settlementTargetManager).should().processPayments(settleDate, emptyList())
        then(settlementTargetManager).should().processCancels(settleDate, emptyList())
    }
}
