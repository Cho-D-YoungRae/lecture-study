package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponTargetRepository
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import org.springframework.stereotype.Component

@Component
class CouponTargetReader(
    private val couponTargetRepository: CouponTargetRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) {
    fun findCouponIdsByProductIds(productIds: Collection<Long>): Set<Long> {
        if (productIds.isEmpty()) return emptySet()

        val productTargets = couponTargetRepository.findByTargetTypeAndTargetIdInAndStatus(
            CouponTargetType.PRODUCT,
            productIds,
            EntityStatus.ACTIVE,
        )

        val categoryIds = productCategoryRepository
            .findByProductIdInAndStatus(productIds, EntityStatus.ACTIVE)
            .map { it.categoryId }

        val categoryTargets = if (categoryIds.isEmpty()) {
            emptyList()
        } else {
            couponTargetRepository
                .findByTargetTypeAndTargetIdInAndStatus(
                    CouponTargetType.PRODUCT_CATEGORY,
                    categoryIds,
                    EntityStatus.ACTIVE,
                )
        }

        return (productTargets + categoryTargets)
            .map { it.couponId }
            .toSet()
    }
}
