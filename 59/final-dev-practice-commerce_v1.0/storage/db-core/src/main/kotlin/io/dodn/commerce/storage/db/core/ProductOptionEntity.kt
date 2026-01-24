package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product_option")
class ProductOptionEntity(
    val productId: Long,
    val name: String,
    val description: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
    val priority: Int,
) : BaseEntity()
