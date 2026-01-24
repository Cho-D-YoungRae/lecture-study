package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartAccessRepository : JpaRepository<CartAccessEntity, Long> {
    fun findByCartIdAndAccessUserIdAndStatus(cartId: Long, accessUserId: Long, status: EntityStatus): CartAccessEntity?
    fun findByUserIdAndStatus(userId: Long, status: EntityStatus): List<CartAccessEntity>
    fun findByAccessKey(accessKey: String): CartAccessEntity?
}
