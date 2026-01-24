package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.storage.db.core.error.IllegalCouponUsageException
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(
    name = "owned_coupon",
    indexes = [
        Index(name = "udx_owned_coupon", columnList = "userId, couponId", unique = true),
    ],
)
class OwnedCouponEntity(
    val userId: Long,
    val couponId: Long,
    state: OwnedCouponState,
    val maxUseCount: Long,
    private var usedCount: Long,
    @Version
    private var version: Long = 0,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: OwnedCouponState = state
        protected set

    fun usedCount(): Long {
        return usedCount
    }

    fun use() {
        if (isFullyUsed()) throw IllegalCouponUsageException("Coupon cannot be used anymore")
        usedCount += 1L
        if (isFullyUsed()) state = OwnedCouponState.USED
    }

    fun revert() {
        if (isUnused()) throw IllegalCouponUsageException("Coupon cannot be reverted because it has not been used")
        usedCount -= 1L
        if (isUnused()) state = OwnedCouponState.DOWNLOADED
    }

    private fun isFullyUsed(): Boolean {
        return (maxUseCount - usedCount) == 0L
    }

    private fun isUnused(): Boolean {
        return usedCount == 0L
    }
}
