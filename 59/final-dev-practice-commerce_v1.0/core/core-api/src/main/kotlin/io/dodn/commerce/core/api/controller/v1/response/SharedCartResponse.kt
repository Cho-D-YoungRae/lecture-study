package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.CartAccess
import io.dodn.commerce.core.enums.CartType
import java.time.LocalDateTime

data class SharedCartResponse(
    val accessKey: String,
    val cartId: Long,
    val type: CartType,
    val expiredAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(accesses: List<CartAccess>): List<SharedCartResponse> {
            return accesses.map { of(it) }
        }

        fun of(access: CartAccess): SharedCartResponse = SharedCartResponse(
            accessKey = access.accessKey,
            cartId = access.cartId,
            type = access.type,
            expiredAt = access.expiredAt,
            createdAt = access.createdAt,
            updatedAt = access.updatedAt,
        )
    }
}
