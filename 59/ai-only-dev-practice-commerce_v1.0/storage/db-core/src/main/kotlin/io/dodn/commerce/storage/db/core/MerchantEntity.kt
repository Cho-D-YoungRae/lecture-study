package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "merchant")
class MerchantEntity(
    val name: String,
    val settlementCycle: Int = 1,
) : BaseEntity()
