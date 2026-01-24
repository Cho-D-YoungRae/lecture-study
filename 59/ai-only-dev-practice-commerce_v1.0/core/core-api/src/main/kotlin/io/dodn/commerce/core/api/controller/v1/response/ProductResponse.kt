package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Product
import java.math.BigDecimal

data class ProductResponse(
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
    val favoriteCount: Long,
    val orderCount: Long,
) {
    companion object {
        fun of(products: List<Product>): List<ProductResponse> {
            return products.map {
                ProductResponse(
                    name = it.name,
                    thumbnailUrl = it.thumbnailUrl,
                    description = it.description,
                    shortDescription = it.shortDescription,
                    costPrice = it.price.costPrice,
                    salesPrice = it.price.salesPrice,
                    discountedPrice = it.price.discountedPrice,
                    favoriteCount = 0L,
                    orderCount = 0L,
                )
            }
        }

        fun of(
            products: List<Product>,
            favoriteCounts: Map<Long, Long>,
            orderCounts: Map<Long, Long>,
        ): List<ProductResponse> {
            return products.map {
                ProductResponse(
                    name = it.name,
                    thumbnailUrl = it.thumbnailUrl,
                    description = it.description,
                    shortDescription = it.shortDescription,
                    costPrice = it.price.costPrice,
                    salesPrice = it.price.salesPrice,
                    discountedPrice = it.price.discountedPrice,
                    favoriteCount = favoriteCounts[it.id] ?: 0L,
                    orderCount = orderCounts[it.id] ?: 0L,
                )
            }
        }
    }
}
