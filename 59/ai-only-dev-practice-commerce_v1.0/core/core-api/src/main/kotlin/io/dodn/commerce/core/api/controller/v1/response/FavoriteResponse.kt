package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Favorite
import io.dodn.commerce.core.domain.Product
import io.dodn.commerce.core.enums.FavoriteTargetType
import java.math.BigDecimal
import java.time.LocalDateTime

data class FavoriteResponse(
    val id: Long,
    val targetType: FavoriteTargetType,
    val targetId: Long,
    val targetName: String? = null,
    val productName: String? = null,
    val productImageUrl: String? = null,
    val costPrice: BigDecimal? = null,
    val salesPrice: BigDecimal? = null,
    val discountedPrice: BigDecimal? = null,
    val favoritedAt: LocalDateTime,
    val isChanged: Boolean = false,
) {
    companion object {
        fun of(favorite: Favorite, product: Product): FavoriteResponse {
            return FavoriteResponse(
                id = favorite.id,
                targetType = FavoriteTargetType.PRODUCT,
                targetId = product.id,
                targetName = product.name,
                productName = product.name,
                productImageUrl = product.thumbnailUrl,
                costPrice = product.price.costPrice,
                salesPrice = product.price.salesPrice,
                discountedPrice = product.price.discountedPrice,
                favoritedAt = favorite.favoritedAt,
                isChanged = product.updatedAt.isAfter(favorite.favoritedAt),
            )
        }

        fun of(favorites: List<Favorite>, productMap: Map<Long, Product>): List<FavoriteResponse> {
            return favorites.map {
                if (it.targetType == FavoriteTargetType.PRODUCT) {
                    of(it, productMap[it.targetId]!!)
                } else {
                    FavoriteResponse(
                        id = it.id,
                        targetType = it.targetType,
                        targetId = it.targetId,
                        favoritedAt = it.favoritedAt,
                        isChanged = false,
                    )
                }
            }
        }

        fun of(
            favorites: List<Favorite>,
            productMap: Map<Long, Product>,
            brandNameMap: Map<Long, String>,
            merchantNameMap: Map<Long, String>,
        ): List<FavoriteResponse> {
            return favorites.map {
                when (it.targetType) {
                    FavoriteTargetType.PRODUCT -> of(it, productMap[it.targetId]!!)
                    FavoriteTargetType.BRAND -> FavoriteResponse(
                        id = it.id,
                        targetType = it.targetType,
                        targetId = it.targetId,
                        targetName = brandNameMap[it.targetId],
                        favoritedAt = it.favoritedAt,
                        isChanged = false,
                    )
                    FavoriteTargetType.MERCHANT -> FavoriteResponse(
                        id = it.id,
                        targetType = it.targetType,
                        targetId = it.targetId,
                        targetName = merchantNameMap[it.targetId],
                        favoritedAt = it.favoritedAt,
                        isChanged = false,
                    )
                }
            }
        }
    }
}
