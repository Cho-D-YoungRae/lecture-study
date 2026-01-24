package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OwnedCouponService(
    private val ownedCouponReader: OwnedCouponReader,
    private val couponDownloader: CouponDownloader,
    private val couponTargetReader: CouponTargetReader,
) {
    fun getOwnedCoupons(user: User): List<OwnedCoupon> {
        return ownedCouponReader.getOwnedCoupons(user.id)
    }

    fun download(user: User, couponId: Long) {
        couponDownloader.download(user.id, couponId)
    }

    fun getOwnedCouponsForCheckout(user: User, productIds: Collection<Long>): List<OwnedCoupon> {
        if (productIds.isEmpty()) return emptyList()
        val applicableCouponIds = couponTargetReader.findCouponIdsByProductIds(productIds)
        if (applicableCouponIds.isEmpty()) return emptyList()

        return ownedCouponReader.findOwnedForCheckout(user.id, applicableCouponIds, LocalDateTime.now())
    }
}
