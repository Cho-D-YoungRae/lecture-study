package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class CancelCalculated(
    val paidAmount: BigDecimal,
    val couponAmount: BigDecimal,
    val pointAmount: BigDecimal,
    val isRestoreCoupon: Boolean,
)
