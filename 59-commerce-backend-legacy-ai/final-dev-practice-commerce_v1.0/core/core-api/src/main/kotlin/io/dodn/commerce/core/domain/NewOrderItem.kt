package io.dodn.commerce.core.domain

data class NewOrderItem(
    val productId: Long,
    val productOptionId: Long,
    val quantity: Long,
)
