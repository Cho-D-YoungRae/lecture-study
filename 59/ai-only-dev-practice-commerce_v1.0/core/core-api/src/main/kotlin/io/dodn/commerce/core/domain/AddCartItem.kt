package io.dodn.commerce.core.domain

data class AddCartItem(
    val productId: Long,
    val productOptionId: Long,
    val quantity: Long,
    val sharedCartId: Long? = null,
)
