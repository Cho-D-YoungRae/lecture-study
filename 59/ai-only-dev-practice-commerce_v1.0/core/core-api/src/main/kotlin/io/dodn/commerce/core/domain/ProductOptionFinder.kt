package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.ProductOptionRepository
import org.springframework.stereotype.Component

@Component
class ProductOptionFinder(
    private val productOptionRepository: ProductOptionRepository,
) {
    fun findOptions(productId: Long): List<ProductOption> {
        return productOptionRepository.findByProductId(productId)
            .filter { it.isActive() }
            .map {
                ProductOption(
                    id = it.id,
                    productId = it.productId,
                    name = it.name,
                    description = it.description,
                    price = Price(
                        costPrice = it.costPrice,
                        salesPrice = it.salesPrice,
                        discountedPrice = it.discountedPrice,
                    ),
                    displayOrder = it.displayOrder,
                )
            }
    }

    fun findActive(ids: List<Long>): List<ProductOption> {
        return productOptionRepository.findAllById(ids)
            .filter { it.isActive() }
            .map {
                ProductOption(
                    id = it.id,
                    productId = it.productId,
                    name = it.name,
                    description = it.description,
                    price = Price(
                        costPrice = it.costPrice,
                        salesPrice = it.salesPrice,
                        discountedPrice = it.discountedPrice,
                    ),
                    displayOrder = it.displayOrder,
                )
            }
            .sortedBy { it.displayOrder }
    }
}
