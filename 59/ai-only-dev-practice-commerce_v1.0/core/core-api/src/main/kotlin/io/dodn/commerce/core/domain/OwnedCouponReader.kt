package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OwnedCouponReader(
    private val ownedCouponRepository: OwnedCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun getOwnedCoupons(userId: Long): List<OwnedCoupon> {
        val owned = ownedCouponRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
        if (owned.isEmpty()) return emptyList()

        val couponMap = couponRepository.findAllById(owned.map { it.couponId }.toSet())
            .associateBy { it.id }

        return owned.map {
            val couponEntity = couponMap[it.couponId]!!
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = couponEntity.id,
                    name = couponEntity.name,
                    type = couponEntity.type,
                    discount = couponEntity.discount,
                    minOrderAmount = couponEntity.minOrderAmount,
                    expiredAt = couponEntity.expiredAt,
                ),
            )
        }
    }

    fun findOwnedForCheckout(userId: Long, couponIds: Collection<Long>, now: LocalDateTime): List<OwnedCoupon> {
        if (couponIds.isEmpty()) return emptyList()

        val owned = ownedCouponRepository.findOwnedCouponIds(userId, couponIds, now)
        if (owned.isEmpty()) return emptyList()

        val couponMap = couponRepository.findAllById(owned.map { it.couponId }.toSet())
            .associateBy { it.id }

        return owned.map {
            val couponEntity = couponMap[it.couponId]!!
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = couponEntity.id,
                    name = couponEntity.name,
                    type = couponEntity.type,
                    discount = couponEntity.discount,
                    minOrderAmount = couponEntity.minOrderAmount,
                    expiredAt = couponEntity.expiredAt,
                ),
            )
        }
    }
}
