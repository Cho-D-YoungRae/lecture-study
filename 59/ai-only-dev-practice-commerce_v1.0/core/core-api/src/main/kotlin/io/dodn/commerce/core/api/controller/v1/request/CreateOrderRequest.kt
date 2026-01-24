package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.NewOrder
import io.dodn.commerce.core.domain.NewOrderItem
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

data class CreateOrderRequest(
    val items: List<Item>,
) {
    data class Item(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Long,
    )

    fun toNewOrder(user: User): NewOrder {
        if (items.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        if (items.any { it.quantity <= 0 }) throw CoreException(ErrorType.INVALID_REQUEST)

        return NewOrder(
            userId = user.id,
            items = items.map {
                NewOrderItem(
                    productId = it.productId,
                    productOptionId = it.productOptionId,
                    quantity = it.quantity,
                )
            },
        )
    }
}
