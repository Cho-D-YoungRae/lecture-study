package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findByUserIdAndIdAndStatus(userId: Long, id: Long, status: EntityStatus): CartItemEntity?
    fun findByCartIdAndProductIdAndProductOptionId(cartId: Long, productId: Long, productOptionId: Long): CartItemEntity?
    fun findByUserIdAndCartIdAndStatus(userId: Long, cartId: Long, status: EntityStatus): List<CartItemEntity>
    fun findByUserIdAndProductOptionIdInAndStatus(userId: Long, productOptionIds: List<Long>, status: EntityStatus): List<CartItemEntity>
}
