package io.dodn.commerce.core.domain

import java.math.BigDecimal
import java.time.LocalDate

data class SettlementTargetSummary(
    val merchantId: Long,
    val settlementDate: LocalDate,
    val targetAmount: BigDecimal,
    val targetCount: Long,
    val orderCount: Long,
)
