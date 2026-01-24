package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<CartEntity, Long> {
    fun findByIdAndStatus(id: Long, status: EntityStatus): CartEntity?
    fun findByOwnerUserIdAndTypeAndStatus(ownerUserId: Long, type: CartType, status: EntityStatus): CartEntity?
}
