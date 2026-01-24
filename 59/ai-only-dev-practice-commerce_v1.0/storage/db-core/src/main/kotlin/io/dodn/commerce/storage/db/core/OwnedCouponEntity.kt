package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.OwnedCouponState
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
    @Version
    private var version: Long = 0,
    private var totalUses: Int = 1,
    private var usedCount: Int = 0,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: OwnedCouponState = state
        protected set

    fun use() {
        // when multi-use enabled, increase usedCount until exhaustion
        if (remainingUses() <= 0) return
        usedCount += 1
        if (remainingUses() == 0) {
            state = OwnedCouponState.USED
        } else {
            state = OwnedCouponState.DOWNLOADED
        }
    }

    fun revert() {
        // revert one use (full cancel case). For partial-cancel proportional policy, integrate higher-level logic to compute units to revert.
        if (usedCount > 0) usedCount -= 1
        state = if (remainingUses() == 0) OwnedCouponState.USED else OwnedCouponState.DOWNLOADED
    }

    fun remainingUses(): Int = totalUses - usedCount
}
