package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderInviteRepository
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class OrderInviteReader(
    private val orderInviteRepository: OrderInviteRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
) {
    fun getOrderInvite(inviteKey: String): OrderInvite {
        val entity = orderInviteRepository.findByInviteKeyAndStatus(inviteKey, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        return OrderInvite(
            orderId = entity.orderId,
            inviteKey = entity.inviteKey,
        )
    }

    fun getOrderByInviteKey(inviteKey: String): Order {
        val invite = getOrderInvite(inviteKey)
        val orderEntity = orderRepository.findByIdOrNull(invite.orderId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (orderEntity.isDeleted()) throw CoreException(ErrorType.NOT_FOUND_DATA)

        val items = orderItemRepository.findByOrderId(orderEntity.id)

        return Order(
            id = orderEntity.id,
            key = orderEntity.orderKey,
            name = orderEntity.name,
            userId = orderEntity.userId,
            totalPrice = orderEntity.totalPrice,
            state = orderEntity.state,
            items = items.map {
                OrderItem(
                    orderId = orderEntity.id,
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
