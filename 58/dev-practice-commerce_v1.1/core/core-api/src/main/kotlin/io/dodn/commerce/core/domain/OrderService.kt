package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderService(
    private val orderKeyGenerator: OrderKeyGenerator,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val productRepository: ProductRepository,
) {
    /**
     * 주문은 ID 를 따로 생성한다 -> auto increment 가 아닌 별도의 키 생성기를 통해서 생성
     * * 보안적인 것
     * * 경쟁사에서 주문 건수 확인 가능
     * * OrderKeyGenerator.generate()는 임의로 만든거라 참고해도되고, 안해도 되고
     *
     * 주문은 만드는 타이밍에 주문 데이터를 생성하는 것이 좋음
     * * 안정적으로 핸들링하고 운영하기 편함
     * * 주문은 중요한 역할을 함
     * * 쓰레기 데이터가 생길 수 있음 -> 하다가 중간에 이탈..
     */
    @Transactional
    fun create(user: User, newOrder: NewOrder): String {
        val orderProductIds = newOrder.items.map { it.productId }.toSet()
        val productMap = productRepository.findByIdInAndStatus(orderProductIds, EntityStatus.ACTIVE).associateBy { it.id }
        if (productMap.isEmpty()) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (productMap.keys != orderProductIds) throw CoreException(ErrorType.PRODUCT_MISMATCH_IN_ORDER)

        val order = OrderEntity(
            userId = user.id,
            orderKey = orderKeyGenerator.generate(),
            name = newOrder.items.first().let { productMap[it.productId]!!.name + if (newOrder.items.size > 1) " 외 ${newOrder.items.size - 1}개" else "" },
            totalPrice = newOrder.items.sumOf { productMap[it.productId]!!.discountedPrice.multiply(BigDecimal.valueOf(it.quantity)) },
            state = OrderState.CREATED,
        )
        val savedOrder = orderRepository.save(order)

        orderItemRepository.saveAll(
            newOrder.items.map {
                val product = productMap[it.productId]!!
                OrderItemEntity(
                    orderId = savedOrder.id,
                    productId = product.id,
                    productName = product.name,
                    thumbnailUrl = product.thumbnailUrl,
                    shortDescription = product.shortDescription,
                    quantity = it.quantity,
                    unitPrice = product.discountedPrice,
                    totalPrice = product.discountedPrice.multiply(BigDecimal.valueOf(it.quantity)),
                )
            },
        )

        return savedOrder.orderKey
    }

    @Transactional
    fun getOrders(user: User): List<OrderSummary> {
        val orders = orderRepository.findByUserIdAndStateAndStatusOrderByIdDesc(user.id, OrderState.PAID, EntityStatus.ACTIVE)
        if (orders.isEmpty()) return emptyList()

        return orders.map {
            OrderSummary(
                id = it.id,
                key = it.orderKey,
                name = it.name,
                userId = user.id,
                totalPrice = it.totalPrice,
                state = it.state,
            )
        }
    }

    @Transactional
    fun getOrder(user: User, orderKey: String, state: OrderState): Order {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, state, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (order.userId != user.id) throw CoreException(ErrorType.NOT_FOUND_DATA)

        val items = orderItemRepository.findByOrderId(order.id)
        if (items.isEmpty()) throw CoreException(ErrorType.NOT_FOUND_DATA)

        return Order(
            id = order.id,
            key = order.orderKey,
            name = order.name,
            userId = user.id,
            totalPrice = order.totalPrice,
            state = order.state,
            items = items.map {
                OrderItem(
                    orderId = order.id,
                    productId = it.productId,
                    productName = it.productName,
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
