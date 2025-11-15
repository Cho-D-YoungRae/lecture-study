package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.ReviewEntity
import io.dodn.commerce.storage.db.core.ReviewRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class ReviewManager(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * 현재는 단순히 저장하고 있지만, 리뷰는 분쟁 요소가 많음 -> 이를 대응하려면 히스토리 필요
     * > 아직은 그런 요구사항이 없는 상황
     */
    fun add(reviewKey: ReviewKey, target: ReviewTarget, content: ReviewContent): Long {
        val saved = reviewRepository.save(
            ReviewEntity(
                userId = reviewKey.user.id,
                reviewKey = reviewKey.key,
                targetType = target.type,
                targetId = target.id,
                rate = content.rate,
                content = content.content,
            ),
        )
        return saved.id
    }

    @Transactional
    fun update(user: User, reviewId: Long, content: ReviewContent): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.rate, content.content)
        return found.id
    }

    @Transactional
    fun delete(user: User, reviewId: Long): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()
        return found.id
    }
}
