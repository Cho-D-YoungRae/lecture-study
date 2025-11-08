package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

/**
 * 왜 ProductService와 분리했을까?
 * * ProductSection은 상품 상세에서만 쓰이는데 Product는 다양한 곳에서 사용됨.
 */
@Service
class ProductSectionService(
    private val productFinder: ProductFinder,
) {
    fun findSections(productId: Long): List<ProductSection> {
        return productFinder.findSections(productId)
    }
}
