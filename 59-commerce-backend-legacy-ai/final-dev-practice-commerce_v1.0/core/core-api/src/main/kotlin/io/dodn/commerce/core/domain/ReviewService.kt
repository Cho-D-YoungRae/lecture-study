package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.file.ImageHandle
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

    fun addReview(user: User, target: ReviewTarget, content: ReviewContent, imageHandle: ImageHandle): Long {
        val reviewKey = reviewPolicyValidator.validateNew(user, target)
        val result = reviewManager.add(reviewKey, target, content, imageHandle)
        pointHandler.earn(user, PointType.REVIEW, result.id, result.pointAmount())
        return result.id
    }

    fun updateReview(user: User, reviewId: Long, content: ReviewContent, imageHandle: ImageHandle): Long {
        reviewPolicyValidator.validateUpdate(user, reviewId)
        return reviewManager.update(user, reviewId, content, imageHandle)
    }

    fun removeReview(user: User, reviewId: Long): Long {
        val result = reviewManager.delete(user, reviewId)
        pointHandler.deduct(user, PointType.REVIEW, result.id, result.pointAmount())
        return result.id
    }
}
