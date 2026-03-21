package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.OrderState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.math.BigDecimal

/**
 * order item 이 product 기준 -> product option 기준으로 변경
 *
 * 마이그레이션이 필요. 기존 product 에 대해 어떤 product option id 사용할 것 인가?
 */
@Entity
@Table(name = "order_item")
class OrderItemEntity(
    val orderId: Long,
    val productId: Long,
    val productOptionId: Long,
    val productName: String,
    val productOptionName: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val quantity: Long,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
    canceledQuantity: Long,
    state: OrderState,
) : BaseEntity() {
    var canceledQuantity: Long = canceledQuantity
        protected set

    @Enumerated(EnumType.STRING)
    var state: OrderState = state
        protected set

    fun paid() {
        this.state = OrderState.PAID
    }

    fun cancel(cancelQuantity: Long) {
        if (this.canceledQuantity + cancelQuantity > this.quantity) {
            throw IllegalArgumentException("Cannot cancel more than ordered quantity")
        }
        this.canceledQuantity += cancelQuantity

        this.state = if (isAllCanceled()) {
            OrderState.CANCELED
        } else {
            OrderState.PARTIAL_CANCELED
        }
    }

    fun cancellableQuantity(): Long {
        return quantity - canceledQuantity
    }

    fun isAllCanceled(): Boolean {
        return quantity == canceledQuantity
    }
}
