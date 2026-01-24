package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.FavoriteTargetType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.FavoriteEntity
import io.dodn.commerce.storage.db.core.FavoriteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class FavoriteManager(
    private val favoriteRepository: FavoriteRepository,
) {
    @Transactional
    fun addFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(user.id, targetType, targetId)
        return if (existing == null) {
            val saved = favoriteRepository.save(
                FavoriteEntity(
                    userId = user.id,
                    targetType = targetType,
                    targetId = targetId,
                    favoritedAt = LocalDateTime.now(),
                ),
            )
            saved.id
        } else {
            existing.favorite()
            existing.id
        }
    }

    @Transactional
    fun removeFavorite(user: User, targetType: FavoriteTargetType, targetId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(user.id, targetType, targetId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        existing.delete()
        return existing.id
    }
}
