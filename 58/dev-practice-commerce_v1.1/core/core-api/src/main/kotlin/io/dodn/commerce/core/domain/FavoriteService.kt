package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.FavoriteEntity
import io.dodn.commerce.storage.db.core.FavoriteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
) {
    /**
     * 30일 이내의 찜만 유지한다는 스펙
     * * 하지만 실제로 찜을 삭제하지는(배치 등을 통해) 않음. -> 데이터가 많이 쌓이지 않고 복잡도를 고려해서 선택(추후 분석용으로 사용도 가능).
     * * * 큰 시스템이라면 분석을 위해 데이터를 동기화 시켜놓거나 하겠지만..
     */
    fun findFavorites(user: User, offsetLimit: OffsetLimit): Page<Favorite> {
        val cutoff = LocalDateTime.now().minusDays(30)
        val result = favoriteRepository.findByUserIdAndStatusAndUpdatedAtAfter(
            user.id,
            EntityStatus.ACTIVE,
            cutoff,
            offsetLimit.toPageable(),
        )
        return Page(
            result.content.map {
                Favorite(
                    id = it.id,
                    userId = it.userId,
                    productId = it.productId,
                    favoritedAt = it.favoritedAt,
                )
            },
            result.hasNext(),
        )
    }

    /**
     * 서비스 개발을 할 때는 hard-delete 를 잘 하지 않음.
     * * 데이터를 삭제하면 추적하거나 데이터로써 분석하기 어려움.
     */
    @Transactional
    fun addFavorite(user: User, productId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndProductId(user.id, productId)
        return if (existing == null) {
            val saved = favoriteRepository.save(
                FavoriteEntity(
                    userId = user.id,
                    productId = productId,
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
    fun removeFavorite(user: User, productId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndProductId(user.id, productId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        existing.delete()
        return existing.id
    }
}
