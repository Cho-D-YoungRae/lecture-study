package io.dodn.commerce.core.domain

data class PartialCancelAction(
    val orderKey: String,
    val orderItemId: Long,
    val quantity: Long,
)
