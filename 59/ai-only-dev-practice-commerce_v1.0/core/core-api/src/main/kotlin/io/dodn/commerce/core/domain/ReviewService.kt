package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewFinder: ReviewFinder,
    private val reviewManager: ReviewManager,
    private val reviewPolicyValidator: ReviewPolicyValidator,
    private val pointHandler: PointHandler,
) {
    fun findRateSummary(target: ReviewTarget): RateSummary {
        return reviewFinder.findRateSummary(target)
    }

    fun findReviews(target: ReviewTarget, offsetLimit: OffsetLimit, imageOnly: Boolean = false): Page<Review> {
        return reviewFinder.find(target, offsetLimit, imageOnly)
    }

    fun addReview(user: User, target: ReviewTarget, content: ReviewContent, images: List<String>): Long {
        val reviewKey = reviewPolicyValidator.validateNew(user, target)
        val reviewId = reviewManager.add(reviewKey, target, content, images)
        pointHandler.earn(user, PointType.REVIEW, reviewId, PointAmount.REVIEW)

        // 이미지가 있으면 추가 포인트 지급
        if (images.isNotEmpty()) {
            pointHandler.earn(user, PointType.REVIEW, reviewId, PointAmount.IMAGE_REVIEW)
        }

        return reviewId
    }

    fun updateReview(user: User, reviewId: Long, content: ReviewContent, images: List<String>, deleteImageIds: List<Long>): Long {
        reviewPolicyValidator.validateUpdate(user, reviewId)
        return reviewManager.update(user, reviewId, content, images, deleteImageIds)
    }

    fun removeReview(user: User, reviewId: Long): Long {
        val hasImages = reviewManager.hasImages(reviewId)
        val deletedReviewId = reviewManager.delete(user, reviewId)
        pointHandler.deduct(user, PointType.REVIEW, deletedReviewId, PointAmount.REVIEW)

        // 이미지가 있었으면 추가 포인트 회수
        if (hasImages) {
            pointHandler.deduct(user, PointType.REVIEW, deletedReviewId, PointAmount.IMAGE_REVIEW)
        }

        return deletedReviewId
    }
}
