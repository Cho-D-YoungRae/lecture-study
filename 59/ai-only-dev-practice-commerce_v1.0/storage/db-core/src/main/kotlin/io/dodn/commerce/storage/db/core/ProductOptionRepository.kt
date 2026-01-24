package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ProductOptionRepository : JpaRepository<ProductOptionEntity, Long> {
    fun findByProductId(productId: Long): List<ProductOptionEntity>
}
