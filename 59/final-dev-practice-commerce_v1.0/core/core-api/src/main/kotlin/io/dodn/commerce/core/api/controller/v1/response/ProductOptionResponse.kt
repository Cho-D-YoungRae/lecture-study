package io.dodn.commerce.core.api.controller.v1.response

import java.math.BigDecimal

data class ProductOptionResponse(
    val id: Long,
    val name: String,
    val description: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
)
