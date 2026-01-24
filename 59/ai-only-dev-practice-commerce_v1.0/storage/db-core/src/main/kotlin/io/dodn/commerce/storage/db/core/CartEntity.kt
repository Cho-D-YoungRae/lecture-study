package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CartType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "cart")
class CartEntity(
    @Column(nullable = false)
    val ownerUserId: Long,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR", nullable = false)
    val type: CartType,
) : BaseEntity()
