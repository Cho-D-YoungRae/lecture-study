package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productFinder: ProductFinder,
    private val productOptionFinder: ProductOptionFinder,
) {
    fun findProducts(categoryId: Long, offsetLimit: OffsetLimit): Page<Product> {
        return productFinder.findByCategory(categoryId, offsetLimit)
    }

    fun findProduct(productId: Long): Product {
        return productFinder.find(productId)
    }

    fun findProducts(productIds: List<Long>): List<Product> {
        return productFinder.find(productIds)
    }

    fun findOptions(productId: Long): List<ProductOption> {
        return productOptionFinder.find(productId)
    }
}
