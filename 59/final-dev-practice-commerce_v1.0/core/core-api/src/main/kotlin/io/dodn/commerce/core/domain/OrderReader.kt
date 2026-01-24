package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderReader(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
) {
    fun getOrders(userId: Long, state: OrderState): List<OrderSummary> {
        val orders = orderRepository.findByUserIdAndStateAndStatusOrderByIdDesc(userId, state, EntityStatus.ACTIVE)
        return orders.map {
            OrderSummary(
                id = it.id,
                key = it.orderKey,
                name = it.name,
                userId = userId,
                totalPrice = it.totalPrice,
                state = it.state,
            )
        }
    }

    fun getOrder(userId: Long, orderKey: String, state: OrderState): Order {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, state, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (order.userId != userId) throw CoreException(ErrorType.NOT_FOUND_DATA)

        val items = orderItemRepository.findByOrderId(order.id)
        if (items.isEmpty()) throw CoreException(ErrorType.NOT_FOUND_DATA)

        return Order(
            id = order.id,
            key = order.orderKey,
            name = order.name,
            userId = userId,
            totalPrice = order.totalPrice,
            state = order.state,
            items = items.map {
                OrderItem(
                    orderId = order.id,
                    productId = it.productId,
                    productOptionId = it.productOptionId,
                    productName = it.productName,
                    productOptionName = it.productOptionName,
                    thumbnailUrl = it.thumbnailUrl,
                    shortDescription = it.shortDescription,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    totalPrice = it.totalPrice,
                )
            },
        )
    }
}
