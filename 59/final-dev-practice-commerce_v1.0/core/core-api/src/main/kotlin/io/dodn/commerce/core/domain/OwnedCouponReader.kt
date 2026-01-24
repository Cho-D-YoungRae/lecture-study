package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.data.repository.findByIdOrNull
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
                maxUseCount = it.maxUseCount,
                usedCount = it.usedCount(),
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

    fun getOwnedCoupon(ownedCouponId: Long): OwnedCoupon {
        val ownedCoupon = ownedCouponRepository.findByIdOrNull(ownedCouponId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val coupon = couponRepository.findByIdOrNull(ownedCoupon.couponId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        return OwnedCoupon(
            id = ownedCoupon.id,
            userId = ownedCoupon.userId,
            state = ownedCoupon.state,
            maxUseCount = ownedCoupon.maxUseCount,
            usedCount = ownedCoupon.usedCount(),
            coupon = Coupon(
                id = coupon.id,
                name = coupon.name,
                type = coupon.type,
                discount = coupon.discount,
                minOrderAmount = coupon.minOrderAmount,
                expiredAt = coupon.expiredAt,

            ),
        )
    }

    fun findOwnedForCheckout(userId: Long, couponIds: Collection<Long>, now: LocalDateTime): List<OwnedCoupon> {
        if (couponIds.isEmpty()) return emptyList()

        val owned = ownedCouponRepository.findOwnedCouponIds(userId, couponIds, now)
        if (owned.isEmpty()) return emptyList()

        val couponMap = couponRepository.findAllById(owned.map { it.couponId }.toSet())
            .associateBy { it.id }

        // NOTE: 쿠폰: 최소주문금액과 사용횟수 검사를 안하는데 무슨 상황일까?
        return owned.map {
            val couponEntity = couponMap[it.couponId]!!
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                maxUseCount = it.maxUseCount,
                usedCount = it.usedCount(),
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
