package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class OrderManager(
    private val orderKeyGenerator: OrderKeyGenerator,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
) {
    @Transactional
    fun create(userId: Long, newOrder: NewOrder, products: List<Product>, productOptions: List<ProductOption>): String {
        if (productOptions.isEmpty()) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (productOptions.size != newOrder.productOptionIds().distinct().size) throw CoreException(ErrorType.PRODUCT_MISMATCH_IN_ORDER)

        val productMap = products.associateBy { it.id }
        val productOptionMap = productOptions.associateBy { it.id }

        val order = OrderEntity(
            userId = userId,
            orderKey = orderKeyGenerator.generate(),
            name = newOrder.items.first().let { productOptionMap[it.productOptionId]!!.name + if (newOrder.items.size > 1) " 외 ${newOrder.items.size - 1}개" else "" },
            totalPrice = newOrder.items.sumOf { productOptionMap[it.productOptionId]!!.price.discountedPrice.multiply(BigDecimal.valueOf(it.quantity)) },
            state = OrderState.CREATED,
        )
        val savedOrder = orderRepository.save(order)

        orderItemRepository.saveAll(
            newOrder.items.map {
                val productOption = productOptionMap[it.productOptionId]!!
                val product = productMap[productOption.productId]!!
                OrderItemEntity(
                    orderId = savedOrder.id,
                    productId = product.id,
                    productOptionId = productOption.id,
                    productName = product.name,
                    productOptionName = productOption.name,
                    thumbnailUrl = product.thumbnailUrl,
                    shortDescription = product.shortDescription,
                    quantity = it.quantity,
                    unitPrice = productOption.price.discountedPrice,
                    totalPrice = productOption.price.discountedPrice.multiply(BigDecimal.valueOf(it.quantity)),
                    canceledQuantity = 0,
                    state = OrderState.CREATED,
                )
            },
        )

        return savedOrder.orderKey
    }
}
