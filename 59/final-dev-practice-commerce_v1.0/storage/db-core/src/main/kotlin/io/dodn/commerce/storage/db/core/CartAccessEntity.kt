package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CartType
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "cart_access",
    indexes = [
        Index(name = "udx_cart_access_key", columnList = "accessKey", unique = true),
        Index(name = "udx_cart_access_user", columnList = "cartId, accessUserId", unique = true),
    ],
)
class CartAccessEntity(
    val accessKey: String,
    val cartId: Long,
    val type: CartType,
    val userId: Long,
    val accessUserId: Long,
    val expiredAt: LocalDateTime,
) : BaseEntity() {
    fun isExpired(): Boolean {
        return expiredAt.isBefore(LocalDateTime.now())
    }

    fun isNotExpired(): Boolean {
        return !isExpired()
    }
}
