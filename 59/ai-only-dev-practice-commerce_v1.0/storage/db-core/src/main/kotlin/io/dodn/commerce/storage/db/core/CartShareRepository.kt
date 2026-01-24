package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartShareRepository : JpaRepository<CartShareEntity, Long> {
    fun findByCartIdAndStatus(cartId: Long, status: EntityStatus): CartShareEntity?
    fun findByTokenAndStatus(token: String, status: EntityStatus): CartShareEntity?
}
