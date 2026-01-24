package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal

@Entity
@Table(
    name = "cancel_balance",
    indexes = [
        Index(name = "udx_cancel_balance_order_id", columnList = "orderId", unique = true),
    ],
)
class CancelBalanceEntity(
    val orderId: Long,
    cancellablePaidAmount: BigDecimal,
    cancellablePointAmount: BigDecimal,
    cancellableCouponAmount: BigDecimal,
    cancelledPaidAmount: BigDecimal = BigDecimal.ZERO,
    cancelledPointAmount: BigDecimal = BigDecimal.ZERO,
    cancelledCouponAmount: BigDecimal = BigDecimal.ZERO,
) : BaseEntity() {
    @Version
    private var version: Long = 0

    var cancellablePaidAmount: BigDecimal = cancellablePaidAmount
        protected set

    var cancellablePointAmount: BigDecimal = cancellablePointAmount
        protected set

    var cancellableCouponAmount: BigDecimal = cancellableCouponAmount
        protected set

    var cancelledPaidAmount: BigDecimal = cancelledPaidAmount
        protected set

    var cancelledPointAmount: BigDecimal = cancelledPointAmount
        protected set

    var cancelledCouponAmount: BigDecimal = cancelledCouponAmount
        protected set

    fun cancel(
        paidAmount: BigDecimal,
        pointAmount: BigDecimal,
        couponAmount: BigDecimal,
    ) {
        this.cancellablePaidAmount = this.cancellablePaidAmount.subtract(paidAmount)
        this.cancellablePointAmount = this.cancellablePointAmount.subtract(pointAmount)
        this.cancellableCouponAmount = this.cancellableCouponAmount.subtract(couponAmount)

        this.cancelledPaidAmount = this.cancelledPaidAmount.add(paidAmount)
        this.cancelledPointAmount = this.cancelledPointAmount.add(pointAmount)
        this.cancelledCouponAmount = this.cancelledCouponAmount.add(couponAmount)
    }
}
