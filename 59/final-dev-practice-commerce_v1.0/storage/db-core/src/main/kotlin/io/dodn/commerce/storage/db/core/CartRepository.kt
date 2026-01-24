package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<CartEntity, Long> {
    fun findByIdAndUserIdAndStatus(id: Long, userId: Long, status: EntityStatus): CartEntity?
    fun findByIdAndTypeAndStatus(id: Long, type: CartType, status: EntityStatus): CartEntity?
    fun findByIdAndStatus(id: Long, status: EntityStatus): CartEntity?
    fun findByUserIdAndTypeAndStatus(userId: Long, type: CartType, status: EntityStatus): List<CartEntity>
}
