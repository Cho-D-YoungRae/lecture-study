package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface OrderInviteRepository : JpaRepository<OrderInviteEntity, Long> {
    fun findByInviteKeyAndStatus(inviteKey: String, status: EntityStatus): OrderInviteEntity?
}
