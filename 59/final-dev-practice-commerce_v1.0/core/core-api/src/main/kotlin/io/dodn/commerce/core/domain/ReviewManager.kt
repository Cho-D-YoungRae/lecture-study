package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.core.support.file.ImageHandle
import io.dodn.commerce.storage.db.core.ImageRepository
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
    private val imageRepository: ImageRepository,
) {
    @Transactional
    fun add(reviewKey: ReviewKey, target: ReviewTarget, content: ReviewContent, imageHandle: ImageHandle): ReviewProcessResult {
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

        if (imageHandle.hasImagesToAdd()) {
            val uploadedImages = imageRepository.findByUserIdAndIdIn(saved.userId, imageHandle.addImageIds)
            if (imageHandle.addImageIds.size != uploadedImages.size) throw CoreException(ErrorType.INVALID_REQUEST)
            reviewImageRepository.saveAll(
                uploadedImages.map {
                    ReviewImageEntity(
                        userId = saved.userId,
                        reviewId = saved.id,
                        imageId = it.id,
                        imageUrl = it.imageUrl,
                    )
                },
            )
        }

        return ReviewProcessResult(saved.id, if (imageHandle.hasImagesToAdd()) ReviewFormat.IMAGE else ReviewFormat.TEXT)
    }

    @Transactional
    fun update(user: User, reviewId: Long, content: ReviewContent, imageHandle: ImageHandle): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.rate, content.content)

        // 기존 리뷰 이미지 불러오기
        val existingImages = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)

        // 지정된 이미지 삭제
        if (imageHandle.hasImagesToDelete()) {
            existingImages.filter { it.id in imageHandle.deleteImageIds }
                .forEach { it.delete() }
        }

        // 기존에 이미지가 있었는데 삭제 후 남은 이미지도 없고 새로운 이미지도 없으면 에러
        if (existingImages.size > 0 && existingImages.count { it.isActive() } <= 0 && imageHandle.hasImagesToAdd() == false) {
            throw CoreException(ErrorType.REVIEW_CANNOT_DELETE_ALL_IMAGES)
        }

        // 새 이미지 추가
        if (imageHandle.hasImagesToAdd()) {
            val uploadedImages = imageRepository.findByUserIdAndIdIn(found.userId, imageHandle.addImageIds)
            if (imageHandle.addImageIds.size != uploadedImages.size) throw CoreException(ErrorType.INVALID_REQUEST)
            reviewImageRepository.saveAll(
                uploadedImages.map {
                    ReviewImageEntity(
                        userId = found.userId,
                        reviewId = found.id,
                        imageId = it.id,
                        imageUrl = it.imageUrl,
                    )
                },
            )
        }
        return found.id
    }

    @Transactional
    fun delete(user: User, reviewId: Long): ReviewProcessResult {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()

        // 이미지도 함께 삭제
        val images = reviewImageRepository.findByReviewIdAndStatus(reviewId, EntityStatus.ACTIVE)
        images.forEach { it.delete() }

        return ReviewProcessResult(found.id, if (images.isEmpty()) ReviewFormat.TEXT else ReviewFormat.IMAGE)
    }
}
