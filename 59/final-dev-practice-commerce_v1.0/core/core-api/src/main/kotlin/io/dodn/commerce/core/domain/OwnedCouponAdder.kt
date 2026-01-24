package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.stereotype.Component

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
