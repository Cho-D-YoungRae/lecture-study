package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.ReviewTargetType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewRepository : JpaRepository<ReviewEntity, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): ReviewEntity?
    fun findByUserIdAndReviewKeyIn(userId: Long, reviewKey: Collection<String>): List<ReviewEntity>
    fun findByTargetTypeAndTargetId(target: ReviewTargetType, targetId: Long): List<ReviewEntity>
    fun findByTargetTypeAndTargetIdAndStatus(target: ReviewTargetType, targetId: Long, status: EntityStatus, slice: Pageable): Slice<ReviewEntity>

    @Query(
        """
        SELECT DISTINCT r FROM ReviewEntity r 
        JOIN ReviewImageEntity ri ON r.id = ri.reviewId 
        WHERE r.targetType = :targetType 
        AND r.targetId = :targetId 
        AND r.status = :status 
        AND ri.status = :imageStatus
    """,
    )
    fun findImageReviewsByTargetTypeAndTargetIdAndStatus(
        targetType: ReviewTargetType,
        targetId: Long,
        status: EntityStatus,
        imageStatus: EntityStatus,
        pageable: Pageable,
    ): Slice<ReviewEntity>
}
