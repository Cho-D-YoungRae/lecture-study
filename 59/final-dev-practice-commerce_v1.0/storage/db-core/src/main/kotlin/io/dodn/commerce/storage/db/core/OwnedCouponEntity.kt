package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.storage.db.core.error.IllegalCouponUsageException
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.Version

/**
 * 엔티티에 로직이 들어있어서 어디가 도메인 객체인가? 헷갈릴 수 있다.
 *
 * 여기있는 로직들을 도메인쪽으로 가져와서 로직 처리를 하고 엔티티는 그냥 값만 반영하도록 변경할 수 있다.
 * > 팀에서 결정하면 됨
 */
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
