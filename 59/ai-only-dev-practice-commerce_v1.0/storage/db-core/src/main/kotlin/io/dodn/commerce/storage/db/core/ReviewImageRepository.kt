package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewImageRepository : JpaRepository<ReviewImageEntity, Long> {
    fun findByReviewIdInAndStatus(reviewIds: Collection<Long>, status: io.dodn.commerce.core.enums.EntityStatus): List<ReviewImageEntity>
    fun findByReviewIdAndStatus(reviewId: Long, status: io.dodn.commerce.core.enums.EntityStatus): List<ReviewImageEntity>
}
