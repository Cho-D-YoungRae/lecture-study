package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OwnedCouponService(
    private val couponRepository: CouponRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    /**
     * 현재는 운영팀이 만약에 쿠폰의 할인금액이나 만료일을 바꾸면 이미 발급된 모든 쿠폰에 적용됨
     * * 이는 장점일 수도 단점일 수도 있음 -> 기획쪽의 요구사항에 따라 변경될 수 있음.
     * * 쿠폰 정보를 변경해도 이미 다운로드 한 사람은 변하지 않도록 하려면 소유 쿠폰쪽에 스냅샷(?)을 저장해야 함.
     */
    fun getOwnedCoupons(user: User): List<OwnedCoupon> {
        val ownedCoupons = ownedCouponRepository.findByUserIdAndStatus(user.id, EntityStatus.ACTIVE)
        if (ownedCoupons.isEmpty()) return emptyList()
        val couponMap = couponRepository.findAllById(ownedCoupons.map { it.couponId }.toSet())
            .associateBy { it.id }

        return ownedCoupons.map {
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = couponMap[it.couponId]!!.id,
                    name = couponMap[it.couponId]!!.name,
                    type = couponMap[it.couponId]!!.type,
                    discount = couponMap[it.couponId]!!.discount,
                    expiredAt = couponMap[it.couponId]!!.expiredAt,
                ),
            )
        }
    }

    fun getOwnedCouponsForCheckout(user: User, productIds: Collection<Long>): List<OwnedCoupon> {
        if (productIds.isEmpty()) return emptyList()
        val applicableCouponMap = couponRepository.findApplicableCouponIds(productIds)
            .associateBy { it.id }

        if (applicableCouponMap.isEmpty()) return emptyList()
        val ownedCoupons = ownedCouponRepository.findOwnedCouponIds(user.id, applicableCouponMap.keys, LocalDateTime.now())

        if (ownedCoupons.isEmpty()) return emptyList()
        return ownedCoupons.map {
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = applicableCouponMap[it.couponId]!!.id,
                    name = applicableCouponMap[it.couponId]!!.name,
                    type = applicableCouponMap[it.couponId]!!.type,
                    discount = applicableCouponMap[it.couponId]!!.discount,
                    expiredAt = applicableCouponMap[it.couponId]!!.expiredAt,
                ),
            )
        }
    }
}
