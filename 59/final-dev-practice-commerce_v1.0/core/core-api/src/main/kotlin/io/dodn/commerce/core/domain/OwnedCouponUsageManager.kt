package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.error.IllegalCouponUsageException
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OwnedCouponUsageManager(
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun use(ownedCouponId: Long) {
        try {
            val ownedCoupon = ownedCouponRepository.findByIdOrNull(ownedCouponId) ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)
            ownedCoupon.use()
        } catch (e: IllegalCouponUsageException) {
            log.error("[OWNED_COUPON_USAGE] 비정상적인 쿠폰 사용 - 사용 완료 쿠폰 ownedCouponId: {}", ownedCouponId, e)
            throw CoreException(ErrorType.OWNED_COUPON_INVALID_USAGE)
        } catch (e: OptimisticLockingFailureException) {
            log.error("[OWNED_COUPON_USAGE] 비정상적인 쿠폰 사용 - 사용 동시성 충돌 ownedCouponId={}", ownedCouponId, e)
            throw CoreException(ErrorType.OWNED_COUPON_INVALID_USAGE)
        }
    }

    @Transactional
    fun revert(ownedCouponId: Long) {
        try {
            val ownedCoupon = ownedCouponRepository.findByIdOrNull(ownedCouponId) ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)
            ownedCoupon.revert()
        } catch (e: IllegalCouponUsageException) {
            log.error("[OWNED_COUPON_USAGE] 비정상적인 쿠폰 복원- 미사용 쿠폰 복원 시도 ownedCouponId: {}", ownedCouponId, e)
            throw CoreException(ErrorType.OWNED_COUPON_INVALID_USAGE)
        } catch (e: OptimisticLockingFailureException) {
            log.error("[OWNED_COUPON_USAGE] 비정상적인 쿠폰 사용 - 복원 동시성 충돌 ownedCouponId={}", ownedCouponId, e)
            throw CoreException(ErrorType.OWNED_COUPON_INVALID_USAGE)
        }
    }
}
