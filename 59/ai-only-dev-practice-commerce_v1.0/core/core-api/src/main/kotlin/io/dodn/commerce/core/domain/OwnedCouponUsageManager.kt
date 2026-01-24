package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class OwnedCouponUsageManager(
    private val ownedCouponRepository: OwnedCouponRepository,
    private val couponRepository: CouponRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 한 주문당 1회 사용 정책. 낙관적 락 충돌 시 즉시 실패 + ERROR 로그.
     */
    @Transactional
    fun useOne(ownedCouponId: Long) {
        try {
            val owned = ownedCouponRepository.findByIdOrNull(ownedCouponId) ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)
            owned.use()
            // JPA dirty checking으로 flush 시도 → @Version으로 충돌 감지
        } catch (e: OptimisticLockingFailureException) {
            log.error("Optimistic locking failure on useOne. ownedCouponId={}", ownedCouponId, e)
            throw CoreException(ErrorType.OWNED_COUPON_INVALID)
        }
    }

    /**
     * 부분취소 비례 복원 정책(B-iii): 실제 결제금액(paidAmount) 대비 환불액(refundAmount)으로 환불 후 남은 금액이
     * 쿠폰의 minOrderAmount를 하회하는 순간 1회 복원. 한 주문당 1회 사용 정책이므로 최대 1회만 복원.
     */
    @Transactional
    fun revertByAmount(ownedCouponId: Long, refundAmount: BigDecimal, paidAmount: BigDecimal) {
        try {
            val owned = ownedCouponRepository.findByIdOrNull(ownedCouponId) ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)

            val coupon = couponRepository.findByIdOrNull(owned.couponId) ?: throw CoreException(ErrorType.COUPON_NOT_FOUND_OR_EXPIRED)
            val minOrder = coupon.minOrderAmount

            val remainingPaid = paidAmount.subtract(refundAmount)
            val needRevert = when {
                minOrder == null -> false
                remainingPaid < BigDecimal.ZERO -> true // 과도 환불 보호: 음수면 무조건 복원
                remainingPaid < minOrder -> true
                else -> false
            }

            if (needRevert) {
                owned.revert()
            }
        } catch (e: OptimisticLockingFailureException) {
            log.error(
                "Optimistic locking failure on revertByAmount. ownedCouponId={}, refundAmount={}, paidAmount={}",
                ownedCouponId,
                refundAmount,
                paidAmount,
                e,
            )
            throw CoreException(ErrorType.OWNED_COUPON_INVALID)
        }
    }
}
