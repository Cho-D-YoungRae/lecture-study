package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class CancelCalculateResult(
    val paidAmount: BigDecimal,
    val couponAmount: BigDecimal,
    val pointAmount: BigDecimal,
    val isRestoreCoupon: Boolean,
)
