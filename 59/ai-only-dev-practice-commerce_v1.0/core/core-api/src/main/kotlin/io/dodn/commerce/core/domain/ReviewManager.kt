package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.ReviewEntity
import io.dodn.commerce.storage.db.core.ReviewImageEntity
import io.dodn.commerce.storage.db.core.ReviewImageRepository
import io.dodn.commerce.storage.db.core.ReviewRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class ReviewManager(
    private val reviewRepository: ReviewRepository,
    private val reviewImageRepository: ReviewImageRepository,
) {
    fun add(reviewKey: ReviewKey, target: ReviewTarget, content: ReviewContent, images: List<String>): Long {
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

        if (images.isNotEmpty()) {
            val imageEntities = images.mapIndexed { index, imageUrl ->
                ReviewImageEntity(
                    reviewId = saved.id,
                    imageUrl = imageUrl,
                    sequence = index,
                )
            }
            reviewImageRepository.saveAll(imageEntities)
        }

        return saved.id
    }

    @Transactional
    fun update(user: User, reviewId: Long, content: ReviewContent, images: List<String>, deleteImageIds: List<Long>): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.rate, content.content)

        // 기존에 이미지가 있었는지 확인
        val existingImages = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)
        val hasExistingImages = existingImages.isNotEmpty()

        // 지정된 이미지 삭제
        if (deleteImageIds.isNotEmpty()) {
            val imagesToDelete = existingImages.filter { it.id in deleteImageIds }
            imagesToDelete.forEach { it.delete() }
        }

        // 삭제 후 남은 이미지 확인
        val remainingImages = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)
        val hasNewImages = images.isNotEmpty()

        // 기존에 이미지가 있었는데 삭제 후 남은 이미지도 없고 새로운 이미지도 없으면 에러
        if (hasExistingImages && remainingImages.isEmpty() && !hasNewImages) {
            throw CoreException(ErrorType.REVIEW_CANNOT_DELETE_ALL_IMAGES)
        }

        // 새 이미지 추가
        if (hasNewImages) {
            val nextSequence = (remainingImages.maxOfOrNull { it.sequence } ?: -1) + 1

            val imageEntities = images.mapIndexed { index, imageUrl ->
                ReviewImageEntity(
                    reviewId = reviewId,
                    imageUrl = imageUrl,
                    sequence = nextSequence + index,
                )
            }
            reviewImageRepository.saveAll(imageEntities)
        }

        return found.id
    }

    @Transactional
    fun delete(user: User, reviewId: Long): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()

        // 이미지도 함께 삭제
        val images = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)
        images.forEach { it.delete() }

        return found.id
    }

    fun hasImages(reviewId: Long): Boolean {
        val images = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)
        return images.isNotEmpty()
    }
}
