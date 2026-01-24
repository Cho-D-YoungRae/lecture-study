package io.dodn.commerce.storage.db.core

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "cart_share")
class CartShareEntity(
    @Column(nullable = false, unique = true)
    val cartId: Long,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,
) : BaseEntity() {
    fun isExpired(now: LocalDateTime = LocalDateTime.now()): Boolean = now.isAfter(expiresAt)
}
