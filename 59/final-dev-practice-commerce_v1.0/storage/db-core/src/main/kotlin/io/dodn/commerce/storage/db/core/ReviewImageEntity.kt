package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

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
