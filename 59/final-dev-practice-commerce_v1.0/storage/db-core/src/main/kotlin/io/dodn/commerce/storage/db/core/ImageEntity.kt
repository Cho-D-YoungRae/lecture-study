package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "image")
class ImageEntity(
    val userId: Long,
    val imageUrl: String,
    val originalFilename: String,
) : BaseEntity()
