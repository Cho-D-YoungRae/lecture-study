package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * @see ImageEntity
 * 이미지 엔티티 있지만 imageUrl 을 반정규화 해둠
 */
@Entity
@Table(
    name = "review_image",
    indexes = [
        Index(name = "idx_review_id", columnList = "reviewId"),
    ],
)
class ReviewImageEntity(
    val userId: Long,
    val reviewId: Long,
    val imageId: Long,
    val imageUrl: String,
) : BaseEntity()
