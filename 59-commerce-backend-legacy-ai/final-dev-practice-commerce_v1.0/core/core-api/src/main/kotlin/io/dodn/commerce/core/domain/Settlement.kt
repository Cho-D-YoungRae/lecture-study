package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.SettlementState
import java.math.BigDecimal
import java.time.LocalDate

data class Settlement(
    val id: Long,
    val merchantId: Long,
    val settlementDate: LocalDate,
    val originalAmount: BigDecimal,
    val feeAmount: BigDecimal,
    val feeRate: BigDecimal,
    val settlementAmount: BigDecimal,
    val state: SettlementState,
)
