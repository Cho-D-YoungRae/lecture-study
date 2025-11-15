package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.AddReviewRequest
import io.dodn.commerce.core.api.controller.v1.request.UpdateReviewRequest
import io.dodn.commerce.core.api.controller.v1.response.ReviewResponse
import io.dodn.commerce.core.domain.ReviewService
import io.dodn.commerce.core.domain.ReviewTarget
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {
    /**
     * 리뷰가 장기적으로 확장될 수 있다는 요구사항이 있는 것
     * * 지금은 상품에만 달릴 수 있지만, 나중에 판매자에게 리뷰를 할 수 있다거나 상품 옵션 등등 혹은 상품 이외 다른 것
     */
    @GetMapping("/v1/reviews")
    fun getReviews(
        @RequestParam targetType: ReviewTargetType,
        @RequestParam targetId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<ReviewResponse>> {
        val page = reviewService.findReviews(ReviewTarget(targetType, targetId), OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(ReviewResponse.of(page.content), page.hasNext))
    }

    @PostMapping("/v1/reviews")
    fun createReview(
        user: User,
        @RequestBody request: AddReviewRequest,
    ): ApiResponse<Any> {
        reviewService.addReview(user, request.toTarget(), request.toContent())
        return ApiResponse.success()
    }

    @PutMapping("/v1/reviews/{reviewId}")
    fun updateReview(
        user: User,
        @PathVariable reviewId: Long,
        @RequestBody request: UpdateReviewRequest,
    ): ApiResponse<Any> {
        reviewService.updateReview(user, reviewId, request.toContent())
        return ApiResponse.success()
    }

    @DeleteMapping("/v1/reviews/{reviewId}")
    fun deleteReview(
        user: User,
        @PathVariable reviewId: Long,
    ): ApiResponse<Any> {
        reviewService.removeReview(user, reviewId)
        return ApiResponse.success()
    }
}
