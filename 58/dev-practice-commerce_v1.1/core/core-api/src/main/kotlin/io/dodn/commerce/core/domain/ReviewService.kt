package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service

/**
 * 리뷰 추가와 업데이트와 같이 로직이 있는 경우에서 응집되어 있는 컴포넌트로 묶었을 때의 장점
 * * 단위 테스트하기 좋음
 * * 트랜잭션 묶기 좋음
 * * 서비스 영역의 코드 보기가 쉬워짐
 */
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

    fun findReviews(target: ReviewTarget, offsetLimit: OffsetLimit): Page<Review> {
        return reviewFinder.find(target, offsetLimit)
    }

    fun addReview(user: User, target: ReviewTarget, content: ReviewContent): Long {
        val reviewKey = reviewPolicyValidator.validateNew(user, target)
        val reviewId = reviewManager.add(reviewKey, target, content)
        pointHandler.earn(user, PointType.REVIEW, reviewId, PointAmount.REVIEW)
        return reviewId
    }

    fun updateReview(user: User, reviewId: Long, content: ReviewContent): Long {
        reviewPolicyValidator.validateUpdate(user, reviewId)
        return reviewManager.update(user, reviewId, content)
    }

    fun removeReview(user: User, reviewId: Long): Long {
        val deletedReviewId = reviewManager.delete(user, reviewId)
        pointHandler.deduct(user, PointType.REVIEW, deletedReviewId, PointAmount.REVIEW)
        return deletedReviewId
    }
}
