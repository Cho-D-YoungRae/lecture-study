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
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
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
        val targetId = request.targetId ?: request.productId ?: throw CoreException(ErrorType.INVALID_REQUEST)

        when (request.type) {
            ApplyFavoriteRequestType.FAVORITE -> favoriteService.addFavorite(user, targetType, targetId)
            ApplyFavoriteRequestType.UNFAVORITE -> favoriteService.removeFavorite(user, targetType, targetId)
        }
    }

    fun getFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<FavoriteResponse> {
        return when (targetType) {
            FavoriteTargetType.PRODUCT -> getProductFavorites(user, targetType, offsetLimit)
            FavoriteTargetType.BRAND -> getBrandFavorites(user, targetType, offsetLimit)
            FavoriteTargetType.MERCHANT -> getMerchantFavorites(user, targetType, offsetLimit)
        }
    }

    private fun getProductFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<FavoriteResponse> {
        val favorites = favoriteService.findFavorites(user, targetType, offsetLimit)
        val productIds = favorites.content.filter { it.targetType == FavoriteTargetType.PRODUCT }
            .map { it.targetId }
            .distinct()
        val productMap = productService.findProducts(productIds).associateBy { it.id }
        return Page(FavoriteResponse.ofProduct(favorites.content, productMap), favorites.hasNext)
    }

    private fun getBrandFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<FavoriteResponse> {
        val favorites = favoriteService.findFavorites(user, targetType, offsetLimit)
        val brandIds = favorites.content.filter { it.targetType == FavoriteTargetType.BRAND }
            .map { it.targetId }
            .distinct()
        val brandNameMap = brandService.find(brandIds).associateBy { it.id }
        return Page(FavoriteResponse.ofBrand(favorites.content, brandNameMap), favorites.hasNext)
    }

    private fun getMerchantFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<FavoriteResponse> {
        val favorites = favoriteService.findFavorites(user, targetType, offsetLimit)
        val merchantIds = favorites.content.filter { it.targetType == FavoriteTargetType.MERCHANT }
            .map { it.targetId }
            .distinct()
        val merchantNameMap = merchantService.find(merchantIds).associateBy { it.id }
        return Page(FavoriteResponse.ofMerchant(favorites.content, merchantNameMap), favorites.hasNext)
    }
}
