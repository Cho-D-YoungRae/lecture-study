package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<ImageEntity, Long> {
    fun findByUserIdAndIdIn(userId: Long, ids: List<Long>): List<ImageEntity>
}
