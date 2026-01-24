package io.dodn.commerce.core.domain

data class AddCartItem(
    val cartId: Long,
    val productId: Long,
    val productOptionId: Long,
    val quantity: Long,
)
