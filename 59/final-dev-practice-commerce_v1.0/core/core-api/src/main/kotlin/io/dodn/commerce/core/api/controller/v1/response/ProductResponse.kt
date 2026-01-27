package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Product
import java.math.BigDecimal

/**
 * unique 뱃지 플래그를 서버에서 해주기로 했음
 * 중요한 개념들에는 침투되지 않도록 가장 바깥의 프레젠테이션 레이어에서 처리할 수 있음
 * > 프레젠테이션 레이어는 서버 것이라기 보다 클라이언트를 위한 것이라고 생각하면 좋다 -> 우리 같은 소규모에서는 타협이 필요할 때가 있을 수 있음(앱 등..)
 * > 규모가 커지면 BFF 등이 클라이언트 쪽에 붙을 수 있음
 */
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
    val isUnique: Boolean,
) {
    companion object {
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
                    isUnique = (favoriteCounts[it.id] ?: 0L) == 0L && (orderCounts[it.id] ?: 0L) == 0L,
                )
            }
        }
    }
}
