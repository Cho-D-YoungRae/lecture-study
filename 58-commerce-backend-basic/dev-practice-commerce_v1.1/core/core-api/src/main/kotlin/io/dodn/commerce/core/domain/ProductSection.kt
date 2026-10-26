package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.ProductSectionType

/**
 * 상품 상세는 동적으로 다양한 정보를 만들어서 보여주고 싶을 수 있음
 */
data class ProductSection(
    val type: ProductSectionType,
    val content: String,
)
