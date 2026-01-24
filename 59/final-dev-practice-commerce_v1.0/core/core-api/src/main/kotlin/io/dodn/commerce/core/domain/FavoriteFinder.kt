package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.storage.db.core.FavoriteRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class FavoriteFinder(
    private val favoriteRepository: FavoriteRepository,
) {
    fun findFavorites(user: User, targetType: FavoriteTargetType, offsetLimit: OffsetLimit): Page<Favorite> {
        val cutoff = LocalDateTime.now().minusDays(30)
        val result = favoriteRepository.findByUserIdAndStatusAndTargetTypeAndUpdatedAtAfter(
            user.id,
            EntityStatus.ACTIVE,
            targetType,
            cutoff,
            offsetLimit.toPageable(),
        )
        return Page(
            result.content.map {
                Favorite(
                    id = it.id,
                    userId = it.userId,
                    targetType = it.targetType,
                    targetId = it.targetId,
                    favoritedAt = it.favoritedAt,
                )
            },
            result.hasNext(),
        )
    }

    fun countByProductIds(productIds: List<Long>, from: LocalDateTime): Map<Long, Long> {
        if (productIds.isEmpty()) {
            return emptyMap()
        }

        val results = favoriteRepository.countByProductIdsAndStatusAndFavoritedAtAfter(
            productIds = productIds,
            targetType = FavoriteTargetType.PRODUCT,
            status = EntityStatus.ACTIVE,
            from = from,
        )

        return results.associate { it.getTargetId() to it.getCount() }
    }
}
