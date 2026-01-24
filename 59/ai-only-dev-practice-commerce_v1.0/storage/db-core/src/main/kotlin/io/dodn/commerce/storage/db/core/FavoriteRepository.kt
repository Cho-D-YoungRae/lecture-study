package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.FavoriteTargetType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface FavoriteRepository : JpaRepository<FavoriteEntity, Long> {
    fun findByUserIdAndTargetTypeAndTargetId(userId: Long, targetType: FavoriteTargetType, targetId: Long): FavoriteEntity?
    fun findByUserIdAndStatusAndUpdatedAtAfter(userId: Long, status: EntityStatus, updatedAtAfter: LocalDateTime, pageable: Pageable): Slice<FavoriteEntity>
    fun findByUserIdAndStatusAndUpdatedAtAfterAndTargetType(userId: Long, status: EntityStatus, updatedAtAfter: LocalDateTime, targetType: FavoriteTargetType, pageable: Pageable): Slice<FavoriteEntity>

    @Query(
        """
        SELECT f.targetId as targetId, COUNT(f) as count 
        FROM FavoriteEntity f 
        WHERE f.targetType = :targetType AND f.targetId IN :productIds 
            AND f.status = :status 
            AND f.favoritedAt >= :from 
        GROUP BY f.targetId
        """,
    )
    fun countByProductIdsAndStatusAndFavoritedAtAfter(
        productIds: List<Long>,
        targetType: FavoriteTargetType,
        status: EntityStatus,
        from: LocalDateTime,
    ): List<TargetCountProjection>
}
