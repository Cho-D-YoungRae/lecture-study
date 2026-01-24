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

class SettlementTargetProcessorTest {
    private val settlementTargetReader: SettlementTargetReader = mock()
    private val settlementTargetManager: SettlementTargetManager = mock()
    private val merchantFinder: MerchantFinder = mock()
    private val settlementTargetProcessor = SettlementTargetProcessor(
        settlementTargetReader,
        settlementTargetManager,
        merchantFinder,
    )

    @Test
    fun `정산 주기에 해당하는 가맹점만 정산 대상으로 적재한다`() {
        // given
        val settleDate = LocalDate.of(2025, 12, 25) // 25일
        val from = LocalDateTime.of(2025, 12, 24, 0, 0)
        val to = LocalDateTime.of(2025, 12, 24, 23, 59, 59)

        val merchantId1 = 1L // 주기 5일 -> 25 % 5 == 0 (대상)
        val merchantId2 = 2L // 주기 7일 -> 25 % 7 != 0 (미대상)
        val merchantId3 = 3L // 주기 1일 -> 25 % 1 == 0 (대상)

        given(merchantFinder.findAll()).willReturn(
            listOf(
                Merchant(id = merchantId1, name = "가맹점1", settlementCycle = 5),
                Merchant(id = merchantId2, name = "가맹점2", settlementCycle = 7),
                Merchant(id = merchantId3, name = "가맹점3", settlementCycle = 1),
            ),
        )

        val pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        given(
            settlementTargetReader.readPaymentsByStateAndPaidAtBetween(
                PaymentState.SUCCESS,
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        given(
            settlementTargetReader.readCancelsByCanceledAtBetween(
                from,
                to,
                pageable,
            ),
        ).willReturn(SliceImpl(emptyList()))

        // when
        settlementTargetProcessor.loadTargets(settleDate, from, to)

        // then
        val expectedMerchantIds = setOf(merchantId1, merchantId3)
        then(settlementTargetManager).should().processPayments(settleDate, emptyList(), expectedMerchantIds)
        then(settlementTargetManager).should().processCancels(settleDate, emptyList(), expectedMerchantIds)
    }

    @Test
    fun `정산 대상 가맹점이 하나도 없으면 프로세스를 종료한다`() {
        // given
        val settleDate = LocalDate.of(2025, 12, 24) // 24일
        val from = LocalDateTime.of(2025, 12, 23, 0, 0)
        val to = LocalDateTime.of(2025, 12, 23, 23, 59, 59)

        given(merchantFinder.findAll()).willReturn(
            listOf(
                Merchant(id = 1L, name = "가맹점1", settlementCycle = 5), // 24 % 5 != 0
                Merchant(id = 2L, name = "가맹점2", settlementCycle = 7), // 24 % 7 != 0
            ),
        )

        // when
        settlementTargetProcessor.loadTargets(settleDate, from, to)

        // then
        then(settlementTargetReader).shouldHaveNoInteractions()
        then(settlementTargetManager).shouldHaveNoInteractions()
    }
}
