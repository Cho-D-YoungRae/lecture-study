package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Brand
import io.dodn.commerce.core.domain.Favorite
import io.dodn.commerce.core.domain.Merchant
import io.dodn.commerce.core.domain.Product
import io.dodn.commerce.core.enums.FavoriteTargetType
import java.math.BigDecimal
import java.time.LocalDateTime

data class FavoriteResponse(
    val id: Long,
    val targetType: FavoriteTargetType,
    val targetId: Long,
    val targetName: String? = null,
    val imageUrl: String? = null,
    val productName: String? = null, // 하위 호환을 위해 필드 유지
    val productImageUrl: String? = null, // 하위 호환을 위해 필드 유지
    val costPrice: BigDecimal? = null,
    val salesPrice: BigDecimal? = null,
    val discountedPrice: BigDecimal? = null,
    val favoritedAt: LocalDateTime,
    val isChanged: Boolean = false,
) {
    companion object {
        fun ofProduct(favorites: List<Favorite>, productMap: Map<Long, Product>): List<FavoriteResponse> {
            return favorites.map {
                val product = productMap[it.targetId]!!
                FavoriteResponse(
                    id = it.id,
                    targetType = FavoriteTargetType.PRODUCT,
                    targetId = product.id, // 앞으로 해당 변수를 사용
                    targetName = product.name, // 앞으로 해당 변수를 사용
                    imageUrl = product.thumbnailUrl, // 앞으로 해당 변수를 사용
                    productName = product.name,
                    productImageUrl = product.thumbnailUrl,
                    costPrice = product.price.costPrice,
                    salesPrice = product.price.salesPrice,
                    discountedPrice = product.price.discountedPrice,
                    favoritedAt = it.favoritedAt,
                    isChanged = product.updatedAt.isAfter(it.favoritedAt),
                )
            }
        }

        fun ofBrand(favorites: List<Favorite>, brandMap: Map<Long, Brand>): List<FavoriteResponse> {
            return favorites.map {
                val brand = brandMap[it.targetId]!!
                FavoriteResponse(
                    id = it.id,
                    targetType = FavoriteTargetType.BRAND,
                    targetId = brand.id,
                    targetName = brand.name,
                    favoritedAt = it.favoritedAt,
                )
            }
        }

        fun ofMerchant(favorites: List<Favorite>, merchantMap: Map<Long, Merchant>): List<FavoriteResponse> {
            return favorites.map {
                val merchant = merchantMap[it.targetId]!!
                FavoriteResponse(
                    id = it.id,
                    targetType = FavoriteTargetType.MERCHANT,
                    targetId = merchant.id,
                    targetName = merchant.name,
                    productName = merchant.name,
                    favoritedAt = it.favoritedAt,
                )
            }
        }
    }
}
