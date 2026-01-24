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
    fun findFavorites(user: User, offsetLimit: OffsetLimit): Page<Favorite> {
        return favoriteFinder.findFavorites(user, offsetLimit, null)
    }

    fun findFavorites(user: User, offsetLimit: OffsetLimit, targetType: FavoriteTargetType?): Page<Favorite> {
        return favoriteFinder.findFavorites(user, offsetLimit, targetType)
    }

    fun addFavorite(user: User, productId: Long): Long {
        return favoriteManager.addFavorite(user, FavoriteTargetType.PRODUCT, productId)
    }

    fun removeFavorite(user: User, productId: Long): Long {
        return favoriteManager.removeFavorite(user, FavoriteTargetType.PRODUCT, productId)
    }

    fun addFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        return favoriteManager.addFavorite(user, targetType, targetId)
    }

    fun removeFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        return favoriteManager.removeFavorite(user, targetType, targetId)
    }

    fun countByProductIds(productIds: List<Long>, from: LocalDateTime): Map<Long, Long> {
        return favoriteFinder.countByProductIds(productIds, from)
    }
}
