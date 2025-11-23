package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.CouponTargetRepository
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponTargetRepository: CouponTargetRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    fun getCouponsForProducts(productIds: Collection<Long>): List<Coupon> {
        val productTargets = couponTargetRepository.findByTargetTypeAndTargetIdInAndStatus(
            CouponTargetType.PRODUCT,
            productIds,
            EntityStatus.ACTIVE,
        )
        val categoryTargets = couponTargetRepository.findByTargetTypeAndTargetIdInAndStatus(
            CouponTargetType.PRODUCT_CATEGORY,
            productCategoryRepository.findByProductIdInAndStatus(productIds, EntityStatus.ACTIVE).map { it.categoryId },
            EntityStatus.ACTIVE,
        )
        return couponRepository.findByIdInAndStatus((productTargets + categoryTargets).map { it.couponId }.toSet(), EntityStatus.ACTIVE)
            .map {
                Coupon(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    discount = it.discount,
                    expiredAt = it.expiredAt,
                )
            }
    }

    /**
     * 현재 다운로드가 굉장히 심플하게 되어있지만 서비스적으로 아쉽긴 함.
     * * 이미 다운로드 받은 쿠폰을 알 수 있도록 하는 방법이 없음.
     */
    fun download(user: User, couponId: Long) {
        val coupon = couponRepository.findByIdAndStatusAndExpiredAtAfter(couponId, EntityStatus.ACTIVE, LocalDateTime.now())
            ?: throw CoreException(ErrorType.COUPON_NOT_FOUND_OR_EXPIRED)

        val existing = ownedCouponRepository.findByUserIdAndCouponId(user.id, couponId)
        if (existing != null) {
            throw CoreException(ErrorType.COUPON_ALREADY_DOWNLOADED)
        }
        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = user.id,
                couponId = coupon.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )
    }
}
