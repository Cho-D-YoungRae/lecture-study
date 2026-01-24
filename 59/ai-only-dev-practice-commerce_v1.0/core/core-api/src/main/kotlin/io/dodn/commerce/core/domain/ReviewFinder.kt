package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.storage.db.core.ReviewImageRepository
import io.dodn.commerce.storage.db.core.ReviewRepository
import org.springframework.stereotype.Component

@Component
class ReviewFinder(
    private val reviewRepository: ReviewRepository,
    private val reviewImageRepository: ReviewImageRepository,
) {
    fun find(target: ReviewTarget, offsetLimit: OffsetLimit, imageOnly: Boolean = false): Page<Review> {
        val result = if (imageOnly) {
            reviewRepository.findImageReviewsByTargetTypeAndTargetIdAndStatus(
                target.type,
                target.id,
                EntityStatus.ACTIVE,
                EntityStatus.ACTIVE,
                offsetLimit.toPageable(),
            )
        } else {
            reviewRepository.findByTargetTypeAndTargetIdAndStatus(
                target.type,
                target.id,
                EntityStatus.ACTIVE,
                offsetLimit.toPageable(),
            )
        }

        val reviewIds = result.content.map { it.id }
        val imageMap = if (reviewIds.isNotEmpty()) {
            reviewImageRepository.findByReviewIdInAndStatus(reviewIds, EntityStatus.ACTIVE)
                .groupBy { it.reviewId }
                .mapValues { entry ->
                    entry.value
                        .sortedBy { it.sequence }
                        .map { ReviewImage(it.imageUrl, it.sequence) }
                }
        } else {
            emptyMap()
        }

        val reviews = result.content.map {
            Review(
                id = it.id,
                userId = it.userId,
                ReviewTarget(
                    type = it.targetType,
                    id = it.targetId,
                ),
                ReviewContent(
                    rate = it.rate,
                    content = it.content,
                ),
                images = imageMap[it.id] ?: emptyList(),
            )
        }

        return Page(
            reviews,
            result.hasNext(),
        )
    }

    fun findRateSummary(target: ReviewTarget): RateSummary {
        val founds = reviewRepository.findByTargetTypeAndTargetId(target.type, target.id).filter { it.isActive() }
        return if (founds.isEmpty()) {
            RateSummary.EMPTY
        } else {
            RateSummary(
                rate = founds.sumOf { it.rate }.divide(founds.size.toBigDecimal()),
                count = founds.size.toLong(),
            )
        }
    }
}
