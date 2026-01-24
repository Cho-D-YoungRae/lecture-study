package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FavoriteService(
    private val favoriteManager: FavoriteManager,
    private val favoriteFinder: FavoriteFinder,
) {
    fun findFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<Favorite> {
        return favoriteFinder.findFavorites(user, targetType, offsetLimit)
    }

    fun addFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        return favoriteManager.addFavorite(user, targetType, targetId)
    }

    fun removeFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        return favoriteManager.removeFavorite(user, targetType, targetId)
    }

    fun recentCount(productIds: List<Long>, from: LocalDateTime): Map<Long, Long> {
        return favoriteFinder.countByProductIds(productIds, from)
    }
}
