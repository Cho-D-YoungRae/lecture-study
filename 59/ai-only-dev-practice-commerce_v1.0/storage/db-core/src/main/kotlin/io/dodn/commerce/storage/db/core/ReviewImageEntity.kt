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
    val reviewId: Long,
    val imageUrl: String,
    val sequence: Int,
) : BaseEntity()
