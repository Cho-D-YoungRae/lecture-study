package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findByUserIdAndStatus(userId: Long, status: EntityStatus): List<CartItemEntity>
    fun findByUserIdAndIdAndStatus(userId: Long, id: Long, status: EntityStatus): CartItemEntity?
    fun findByUserIdAndProductId(userId: Long, productId: Long): CartItemEntity?

    // cartId 기반(공유/개인 Cart 일원화 경로)
    fun findByCartIdAndProductId(cartId: Long, productId: Long): CartItemEntity?
    fun findByCartIdAndIdAndStatus(cartId: Long, id: Long, status: EntityStatus): CartItemEntity?
    fun findByCartIdAndStatus(cartId: Long, status: EntityStatus): List<CartItemEntity>
}
