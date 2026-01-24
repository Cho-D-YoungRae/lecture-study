package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import java.math.BigDecimal
import java.time.LocalDateTime

data class Payment(
    val id: Long,
    val userId: Long,
    val orderId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val paidAmount: BigDecimal,
    val state: PaymentState,
    val externalPaymentKey: String?,
    val method: PaymentMethod?,
    val approveCode: String?,
    val paidAt: LocalDateTime?,
)
