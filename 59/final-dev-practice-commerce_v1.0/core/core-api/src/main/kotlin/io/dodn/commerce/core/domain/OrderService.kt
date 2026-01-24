package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderFinder: OrderFinder,
    private val orderReader: OrderReader,
    private val orderManager: OrderManager,
    private val productFinder: ProductFinder,
    private val productOptionFinder: ProductOptionFinder,
    private val orderInviteManager: OrderInviteManager,
    private val orderInviteReader: OrderInviteReader,
) {
    fun create(user: User, newOrder: NewOrder): String {
        val productOptions = productOptionFinder.find(newOrder.productOptionIds(), EntityStatus.ACTIVE)
        val products = productFinder.find(productOptions.map { it.productId }, EntityStatus.ACTIVE)

        return orderManager.create(
            userId = user.id,
            newOrder = newOrder,
            products = products,
            productOptions = productOptions,
        )
    }

    fun getOrders(user: User): List<OrderSummary> {
        return orderReader.getOrders(user.id, OrderState.PAID)
    }

    fun getOrder(user: User, orderKey: String, state: OrderState): Order {
        return orderReader.getOrder(user.id, orderKey, state)
    }

    fun recentCount(productIds: List<Long>, fromDate: LocalDateTime): Map<Long, Long> {
        return orderFinder.countOrdersByProductIds(productIds, fromDate)
    }

    fun createInvite(user: User, orderKey: String): String {
        val order = orderReader.getOrder(user.id, orderKey, OrderState.CREATED)
        return orderInviteManager.create(order.id)
    }

    fun getOrderByInvite(inviteKey: String): Order {
        return orderInviteReader.getOrderByInviteKey(inviteKey)
    }
}
