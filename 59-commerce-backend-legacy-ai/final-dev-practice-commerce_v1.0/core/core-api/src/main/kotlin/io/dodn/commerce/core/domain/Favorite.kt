package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.FavoriteTargetType
import java.time.LocalDateTime

data class Favorite(
    val id: Long,
    val userId: Long,
    val targetType: FavoriteTargetType,
    val targetId: Long,
    val favoritedAt: LocalDateTime,
)
