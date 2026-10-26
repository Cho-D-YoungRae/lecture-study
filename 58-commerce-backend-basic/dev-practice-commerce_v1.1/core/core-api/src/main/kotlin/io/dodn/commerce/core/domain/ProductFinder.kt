package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import io.dodn.commerce.storage.db.core.ProductRepository
import io.dodn.commerce.storage.db.core.ProductSectionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

/**
 * 모든 테이블에 EntityStatus 가 존재하는데 ProductCategoryEntity 는 ACTIVE 인 것을 조회하는데, ProductEntity 는 조회 조건 없음
 * > ProductCategory 에 들어가 있는 Product 는 삭제될 수 없다는 정의가 되어있는 것임
 * > Product 가 삭제되려면 모든 ProductCategory 가 지워져야 한다는 룰
 *
 * Product 와 ProductCategory 모두 삭제 체크를 할 수 있지만 그러면 번거로워 짐
 *
 * 구현 방법(여기서는 1번 사용)
 * 1. ProductCategory 가 1개라도 있으면 Product 를 삭제 못하게 한다
 * 2. Product 를 삭제할 때 모든 ProductCategory 를 삭제한다
 * 3. ... 등 여러가지가 더 있을 수 있다
 */
@Component
class ProductFinder(
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val productSectionRepository: ProductSectionRepository,
) {
    fun findByCategory(categoryId: Long, offsetLimit: OffsetLimit): Page<Product> {
        val categories = productCategoryRepository.findByCategoryIdAndStatus(categoryId, EntityStatus.ACTIVE, offsetLimit.toPageable())
        val products = productRepository.findAllById(categories.content.map { it.productId })
            .map {
                Product(
                    id = it.id,
                    name = it.name,
                    thumbnailUrl = it.thumbnailUrl,
                    description = it.description,
                    shortDescription = it.shortDescription,
                    price = Price(
                        costPrice = it.costPrice,
                        salesPrice = it.salesPrice,
                        discountedPrice = it.discountedPrice,
                    ),
                )
            }
        return Page(products, categories.hasNext())
    }

    fun find(productId: Long): Product {
        val found = productRepository.findByIdOrNull(productId)?.takeIf { it.isActive() }
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        return Product(
            id = found.id,
            name = found.name,
            thumbnailUrl = found.thumbnailUrl,
            description = found.description,
            shortDescription = found.shortDescription,
            price = Price(
                costPrice = found.costPrice,
                salesPrice = found.salesPrice,
                discountedPrice = found.discountedPrice,
            ),
        )
    }

    fun findSections(productId: Long): List<ProductSection> {
        return productSectionRepository.findByProductId(productId)
            .filter { it.isActive() }
            .map { ProductSection(it.type, it.content) }
    }
}
