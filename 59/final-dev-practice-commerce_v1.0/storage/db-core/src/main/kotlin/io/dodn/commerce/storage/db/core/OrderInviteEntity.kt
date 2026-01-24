package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "order_invite",
    indexes = [
        Index(name = "udx_order_invite_key", columnList = "inviteKey", unique = true),
        Index(name = "idx_order_id", columnList = "orderId"),
    ],
)
class OrderInviteEntity(
    val orderId: Long,
    val inviteKey: String,
) : BaseEntity()
