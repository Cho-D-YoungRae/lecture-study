package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.FavoriteTargetType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "favorite")
class FavoriteEntity(
    val userId: Long,
    @Enumerated(EnumType.STRING)
    val targetType: FavoriteTargetType,
    val targetId: Long,
    favoritedAt: LocalDateTime,
) : BaseEntity() {
    var favoritedAt: LocalDateTime = favoritedAt
        protected set

    fun favorite() {
        active()
        favoritedAt = LocalDateTime.now()
    }
}
