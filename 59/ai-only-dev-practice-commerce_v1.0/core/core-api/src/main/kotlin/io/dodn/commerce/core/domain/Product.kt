package io.dodn.commerce.core.domain

import java.time.LocalDateTime

data class Product(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val price: Price,
    val updatedAt: LocalDateTime,
)
