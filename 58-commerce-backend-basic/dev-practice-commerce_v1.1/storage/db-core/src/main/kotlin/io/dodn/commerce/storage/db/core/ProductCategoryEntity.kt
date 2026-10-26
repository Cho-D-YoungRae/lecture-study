package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * 카테고리 안에 상품이 있어서 CategoryProductEntity 라고 생각할 수 있는데
 * 카테고리는 부수적인 개념이라고 생각돼서 ProductCategoryEntity 라고 명명함
 */
@Entity
@Table(name = "product_category")
class ProductCategoryEntity(
    val productId: Long,
    val categoryId: Long,
) : BaseEntity()
