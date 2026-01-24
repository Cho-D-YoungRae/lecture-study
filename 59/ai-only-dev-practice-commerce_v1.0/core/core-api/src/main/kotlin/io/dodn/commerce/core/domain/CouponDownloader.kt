package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponDownloader(
    private val couponRepository: CouponRepository,
    private val ownedCouponAdder: OwnedCouponAdder,
) {
    fun download(userId: Long, couponId: Long) {
        val coupon = couponRepository.findByIdAndStatusAndExpiredAtAfter(
            couponId,
            EntityStatus.ACTIVE,
            LocalDateTime.now(),
        ) ?: throw CoreException(ErrorType.COUPON_NOT_FOUND_OR_EXPIRED)

        ownedCouponAdder.addIfNotExists(userId, coupon.id)
    }
}
