package io.dodn.commerce.core.domain

data class Merchant(
    val id: Long,
    val name: String,
    val settlementCycle: Int,
)
