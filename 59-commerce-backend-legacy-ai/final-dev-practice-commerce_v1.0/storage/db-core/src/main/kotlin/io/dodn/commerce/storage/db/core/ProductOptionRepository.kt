package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOptionRepository : JpaRepository<ProductOptionEntity, Long> {
    fun findByProductId(productId: Long): List<ProductOptionEntity>
    fun findByIdInAndStatus(productIds: List<Long>, status: EntityStatus): List<ProductOptionEntity>
}
