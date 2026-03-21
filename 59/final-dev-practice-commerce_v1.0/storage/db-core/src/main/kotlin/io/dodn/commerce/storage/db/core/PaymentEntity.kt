package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 조르기를 추가하면서 주문을 만든 사람과 결제한 사람이 달라질 수 있음 -> payerUserId 추가
 */
@Entity
@Table(
    name = "payment",
    indexes = [
        Index(name = "udx_order_id", columnList = "orderId", unique = true),
    ],
)
class PaymentEntity(
    val userId: Long,
    val orderId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val payerUserId: Long,
    val paidAmount: BigDecimal,
    state: PaymentState,
    externalPaymentKey: String? = null,
    method: PaymentMethod? = null,
    approveCode: String? = null,
    paidAt: LocalDateTime? = null,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: PaymentState = state
        protected set

    var externalPaymentKey: String? = externalPaymentKey
        protected set

    var method: PaymentMethod? = method
        protected set

    var approveCode: String? = approveCode
        protected set

    var paidAt: LocalDateTime? = paidAt
        protected set

    fun success(externalPaymentKey: String, method: PaymentMethod, approveCode: String) {
        this.state = PaymentState.SUCCESS
        this.externalPaymentKey = externalPaymentKey
        this.method = method
        this.approveCode = approveCode
        this.paidAt = LocalDateTime.now()
    }

    fun hasAppliedCoupon(): Boolean {
        return ownedCouponId > 0
    }
}
