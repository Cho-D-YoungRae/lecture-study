package io.dodn.commerce.core.domain

data class ProductOption(
    val id: Long,
    val productId: Long,
    val name: String,
    val description: String,
    val price: Price,
    val displayOrder: Int,
)
