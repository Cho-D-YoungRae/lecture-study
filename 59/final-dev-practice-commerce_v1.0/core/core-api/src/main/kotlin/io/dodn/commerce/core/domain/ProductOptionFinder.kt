package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.ProductOptionRepository
import org.springframework.stereotype.Component

@Component
class ProductOptionFinder(
    private val productOptionRepository: ProductOptionRepository,
) {
    fun find(productId: Long): List<ProductOption> {
        return productOptionRepository.findByProductId(productId)
            .filter { it.isActive() }
            .sortedBy { it.priority }
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
                )
            }
    }

    fun find(ids: List<Long>, status: EntityStatus): List<ProductOption> {
        return productOptionRepository.findByIdInAndStatus(ids, status)
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
                )
            }
    }
}
