package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.assembler.FavoriteAssembler
import io.dodn.commerce.core.api.controller.v1.request.ApplyFavoriteRequest
import io.dodn.commerce.core.api.controller.v1.response.FavoriteResponse
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FavoriteController(
    private val favoriteAssembler: FavoriteAssembler,
) {
    @GetMapping("/v1/favorites")
    fun getFavorites(
        user: User,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        @RequestParam(required = false, defaultValue = "PRODUCT") targetType: FavoriteTargetType,
    ): ApiResponse<PageResponse<FavoriteResponse>> {
        val page = favoriteAssembler.getFavorites(user, targetType, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(page.content, page.hasNext))
    }

    @PostMapping("/v1/favorites")
    fun applyFavorite(
        user: User,
        @RequestBody request: ApplyFavoriteRequest,
    ): ApiResponse<Any> {
        favoriteAssembler.applyFavorite(user, request)
        return ApiResponse.success()
    }
}
