package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OrderState
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class PaymentPostProcessor(
    private val orderReader: OrderReader,
    private val cartItemManager: CartItemManager,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    fun process(userId: Long, orderKey: String) {
        try {
            log.info("[PAYMENT_POST_PROCESS] Start processing for userId: {}, orderKey: {}", userId, orderKey)
            val order = orderReader.getOrder(userId, orderKey, OrderState.PAID)
            val productOptionIds = order.items.map { it.productOptionId }

            cartItemManager.deleteItemsByProductOptions(userId, productOptionIds)
            log.info("[PAYMENT_POST_PROCESS] Successfully removed cart items for userId: {}, orderKey: {}", userId, orderKey)
        } catch (e: Exception) {
            log.error("[PAYMENT_POST_PROCESS] Error processing for userId: {}, orderKey: {}", userId, orderKey, e)
        }
    }
}
