package io.dodn.commerce.core.api.assembler

import io.dodn.commerce.core.api.controller.v1.request.ApplyFavoriteRequest
import io.dodn.commerce.core.api.controller.v1.request.ApplyFavoriteRequestType
import io.dodn.commerce.core.api.controller.v1.response.FavoriteResponse
import io.dodn.commerce.core.domain.BrandService
import io.dodn.commerce.core.domain.FavoriteService
import io.dodn.commerce.core.domain.MerchantService
import io.dodn.commerce.core.domain.ProductService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Component

@Component
class FavoriteAssembler(
    private val favoriteService: FavoriteService,
    private val productService: ProductService,
    private val brandService: BrandService,
    private val merchantService: MerchantService,
) {
    fun applyFavorite(user: User, request: ApplyFavoriteRequest) {
        // 호환 처리: 기존 클라이언트는 productId만 보낼 수 있음
        val targetType = request.targetType ?: FavoriteTargetType.PRODUCT
        val targetId = request.targetId ?: request.productId
            ?: throw IllegalArgumentException("targetId 또는 productId 중 하나는 필수입니다")

        when (request.type) {
            ApplyFavoriteRequestType.FAVORITE -> favoriteService.addFavorite(user, targetType, targetId)
            ApplyFavoriteRequestType.UNFAVORITE -> favoriteService.removeFavorite(user, targetType, targetId)
        }
    }

    fun getFavorites(user: User, offsetLimit: OffsetLimit, targetType: FavoriteTargetType?): Page<FavoriteResponse> {
        val page = favoriteService.findFavorites(user, offsetLimit, targetType)

        val productIds = page.content.filter { it.targetType == FavoriteTargetType.PRODUCT }
            .map { it.targetId }
            .distinct()
        val brandIds = page.content.filter { it.targetType == FavoriteTargetType.BRAND }
            .map { it.targetId }
            .distinct()
        val merchantIds = page.content.filter { it.targetType == FavoriteTargetType.MERCHANT }
            .map { it.targetId }
            .distinct()

        val productMap = productService.findProducts(productIds).associateBy { it.id }
        val brandNameMap = brandService.findByIds(brandIds).associate { it.id to it.name }
        val merchantNameMap = merchantService.findByIds(merchantIds).associate { it.id to it.name }

        return Page(
            FavoriteResponse.of(page.content, productMap, brandNameMap, merchantNameMap),
            page.hasNext,
        )
    }
}
