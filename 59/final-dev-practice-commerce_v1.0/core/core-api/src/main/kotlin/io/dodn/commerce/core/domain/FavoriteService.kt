package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * ## repository 참조 정리
 *
 * 레거시에서 service 에서도 repository 참조하고 finder 에서도 repository 참조하는 구조 있었음 -> 개선
 *
 * ```kotlin
 * @Service
 * class FavoriteService(
 *     private val favoriteFinder: FavoriteFinder,
 *     private val favoriteRepository: FavoriteRepository,
 * ) {}
 *
 * class FavoriteFinder(
 *     private val favoriteRepository: FavoriteRepository,
 * ) {}
 * ```
 *
 * ## finder vs reader
 *
 * > 강사의 규칙
 *
 * - reader: 단순한 읽기를 할 때 > 1건 조회하거나
 * - finder: 찾는 행위를 할 때 > 단순하지 않은 쿼리를 사용하거나 여러 컴포넌트를 조합해서 어떤 정보를 찾을 때
 */
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
