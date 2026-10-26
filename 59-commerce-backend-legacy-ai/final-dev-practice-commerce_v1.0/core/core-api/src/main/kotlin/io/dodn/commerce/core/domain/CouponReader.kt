package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponRepository
import org.springframework.stereotype.Component

@Component
class CouponReader(
    private val couponRepository: CouponRepository,
) {
    fun findActiveByIds(couponIds: Collection<Long>): List<Coupon> {
        if (couponIds.isEmpty()) return emptyList()
        return couponRepository.findByIdInAndStatus(couponIds, EntityStatus.ACTIVE)
            .map {
                Coupon(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    discount = it.discount,
                    minOrderAmount = it.minOrderAmount,
                    expiredAt = it.expiredAt,
                )
            }
    }
}
