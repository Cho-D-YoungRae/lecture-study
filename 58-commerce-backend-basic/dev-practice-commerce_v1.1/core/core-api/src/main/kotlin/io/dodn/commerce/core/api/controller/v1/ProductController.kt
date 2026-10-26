package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.response.ProductDetailResponse
import io.dodn.commerce.core.api.controller.v1.response.ProductResponse
import io.dodn.commerce.core.domain.CouponService
import io.dodn.commerce.core.domain.ProductSectionService
import io.dodn.commerce.core.domain.ProductService
import io.dodn.commerce.core.domain.ReviewService
import io.dodn.commerce.core.domain.ReviewTarget
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService,
    private val productSectionService: ProductSectionService,
    private val reviewService: ReviewService,
    private val couponService: CouponService,
) {
    @GetMapping("/v1/products")
    fun findProducts(
        @RequestParam categoryId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<ProductResponse>> {
        val result = productService.findProducts(categoryId, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(ProductResponse.of(result.content), result.hasNext))
    }

    /**
     * 컨트롤러에서는 클라이언트에 맞게 데이터를 조합하거나 가공해서 보여주기도 함.
     *
     * 상품 상세에서 사용하는데 그러면 평점도 상품 서비스에 넣어줘도 되려나?
     * * 그러면 상품 서비스가 리뷰를 알게되는 것
     * * 개념이 달라서 격벽이 쳐져있는데 상품 -> 리뷰 알게 되는것
     * * 현재는 상품이 리뷰 자체를 모름. 상품이 리뷰를 알 필요가 없음.
     */
    @GetMapping("/v1/products/{productId}")
    fun findProduct(
        @PathVariable productId: Long,
    ): ApiResponse<ProductDetailResponse> {
        val product = productService.findProduct(productId)
        val sections = productSectionService.findSections(productId)
        val rateSummary = reviewService.findRateSummary(ReviewTarget(ReviewTargetType.PRODUCT, productId))
        // NOTE: 별도 API 가 나을까?
        val coupons = couponService.getCouponsForProducts(listOf(productId))
        return ApiResponse.success(ProductDetailResponse(product, sections, rateSummary, coupons))
    }
}
