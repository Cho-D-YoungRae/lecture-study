package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CancelType
import java.math.BigDecimal
import java.time.LocalDateTime

data class SettlementCancel(
    val id: Long,
    val userId: Long,
    val type: CancelType,
    val orderId: Long,
    val orderItemId: Long,
    val paymentId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val paidAmount: BigDecimal,
    val canceledQuantity: Long,
    val canceledPaidAmount: BigDecimal,
    val canceledPointAmount: BigDecimal,
    val canceledCouponAmount: BigDecimal,
    val externalCancelKey: String,
    val canceledAt: LocalDateTime,
)
