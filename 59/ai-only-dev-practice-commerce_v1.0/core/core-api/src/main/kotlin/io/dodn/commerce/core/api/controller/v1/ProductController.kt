package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.assembler.ProductAssembler
import io.dodn.commerce.core.api.controller.v1.response.ProductDetailResponse
import io.dodn.commerce.core.api.controller.v1.response.ProductResponse
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productAssembler: ProductAssembler,
) {
    @GetMapping("/v1/products")
    fun findProducts(
        @RequestParam categoryId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<ProductResponse>> {
        val result = productAssembler.findProducts(categoryId, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(result.content, result.hasNext))
    }

    @GetMapping("/v1/products/{productId}")
    fun findProduct(
        @PathVariable productId: Long,
    ): ApiResponse<ProductDetailResponse> {
        return ApiResponse.success(productAssembler.assembleProductDetail(productId))
    }
}
