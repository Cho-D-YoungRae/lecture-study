package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class OrderItem(
    val orderId: Long,
    val productId: Long,
    val productOptionId: Long,
    val productName: String,
    val productOptionName: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val quantity: Long,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
)
