package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import java.time.LocalDateTime

data class CartAccess(
    val accessKey: String,
    val cartId: Long,
    val type: CartType,
    val userId: Long,
    val expiredAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
