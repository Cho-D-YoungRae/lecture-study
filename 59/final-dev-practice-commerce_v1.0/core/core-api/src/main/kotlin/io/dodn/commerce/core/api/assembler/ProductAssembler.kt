package io.dodn.commerce.core.api.assembler

import io.dodn.commerce.core.api.controller.v1.response.ProductDetailResponse
import io.dodn.commerce.core.api.controller.v1.response.ProductResponse
import io.dodn.commerce.core.domain.CouponService
import io.dodn.commerce.core.domain.FavoriteService
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.ProductSectionService
import io.dodn.commerce.core.domain.ProductService
import io.dodn.commerce.core.domain.ReviewService
import io.dodn.commerce.core.domain.ReviewTarget
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ProductAssembler(
    private val productService: ProductService,
    private val productSectionService: ProductSectionService,
    private val reviewService: ReviewService,
    private val couponService: CouponService,
    private val favoriteService: FavoriteService,
    private val orderService: OrderService,
) {
    fun findProducts(categoryId: Long, offsetLimit: OffsetLimit): Page<ProductResponse> {
        val productPage = productService.findProducts(categoryId, offsetLimit)
        val productIds = productPage.content.map { it.id }

        val now = LocalDateTime.now()
        val favoriteCountMap = favoriteService.recentCount(productIds, now.minusDays(ProductAssembleSpec.FAVORITE_COUNT_DAYS))
        val orderCountMap = orderService.recentCount(productIds, now.minusDays(ProductAssembleSpec.ORDER_COUNT_DAYS))

        val responses = ProductResponse.of(productPage.content, favoriteCountMap, orderCountMap)
        return Page(responses, productPage.hasNext)
    }

    fun findProduct(productId: Long): ProductDetailResponse {
        val product = productService.findProduct(productId)
        val options = productService.findOptions(productId)
        val sections = productSectionService.findSections(productId)
        val rateSummary = reviewService.findRateSummary(ReviewTarget(ReviewTargetType.PRODUCT, productId))
        val coupons = couponService.getCouponsForProducts(listOf(productId))
        return ProductDetailResponse(product, sections, options, rateSummary, coupons)
    }
}
