package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Coupon 에 대한 것은 직접적으로 자신의 repository 를 사용
 *
 * OwnedCoupon 은 같은 개념 격벽 안에 들어 있긴 하지만 다른 개념이므로 Adder 라는 다른 컴포넌트를 사용
 *
 * > 자기 개념에 대해서만 레파지토리로 직접 접근하고 자기 개념이 아닌 것은 컴포넌트로 접근한다는 규칙을 만들 수 있지만 아직 그런 규칙을 강제하기는 성숙도가 높지 않을 수 있음
 */
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

        ownedCouponAdder.addIfNotExists(userId, coupon.id, coupon.maxUseCount)
    }
}
