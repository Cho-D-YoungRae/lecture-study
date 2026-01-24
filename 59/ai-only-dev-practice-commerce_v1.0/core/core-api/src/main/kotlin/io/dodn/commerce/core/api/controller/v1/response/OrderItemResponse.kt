package io.dodn.commerce.core.api.controller.v1.response

import java.math.BigDecimal

data class OrderItemResponse(
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
