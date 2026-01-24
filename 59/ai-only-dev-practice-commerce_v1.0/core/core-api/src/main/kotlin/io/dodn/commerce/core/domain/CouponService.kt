package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponTargetReader: CouponTargetReader,
    private val couponReader: CouponReader,
) {
    fun getCouponsForProducts(productIds: Collection<Long>): List<Coupon> {
        val couponIds = couponTargetReader.findCouponIdsByProductIds(productIds)
        return couponReader.findActiveByIds(couponIds)
    }
}
