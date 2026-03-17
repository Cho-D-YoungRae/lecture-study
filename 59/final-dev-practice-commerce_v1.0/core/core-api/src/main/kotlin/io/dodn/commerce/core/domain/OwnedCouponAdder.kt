package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.stereotype.Component

/**
 * 쿠폰을 추가하는 간단한 기능을 함
 *
 * - 책임이 작아져서 테스트하기도 쉬움
 * - 밸리데이션이 없기 때문에 잘못 사용될 수 있음
 */
@Component
class OwnedCouponAdder(
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    fun addIfNotExists(userId: Long, couponId: Long, maxUseCount: Long) {
        val existing = ownedCouponRepository.findByUserIdAndCouponId(userId, couponId)
        if (existing != null) {
            throw CoreException(ErrorType.COUPON_ALREADY_DOWNLOADED)
        }

        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = userId,
                couponId = couponId,
                state = OwnedCouponState.DOWNLOADED,
                maxUseCount = maxUseCount,
                usedCount = 0,
            ),
        )
    }
}
