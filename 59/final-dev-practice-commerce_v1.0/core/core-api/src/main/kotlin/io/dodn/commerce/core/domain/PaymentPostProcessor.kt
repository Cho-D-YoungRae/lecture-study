package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class PaymentPostProcessor(
    private val paymentRepository: PaymentRepository,
    private val orderReader: OrderReader,
    private val cartItemManager: CartItemManager,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    fun process(paymentId: Long, orderKey: String) {
        try {
            val payment = paymentRepository.findByIdOrNull(paymentId)
                ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
            if (payment.state != PaymentState.SUCCESS) throw CoreException(ErrorType.PAYMENT_INVALID_STATE)

            val order = orderReader.getOrder(payment.userId, orderKey, OrderState.PAID)
            cartItemManager.deleteItemsByProductOptions(payment.userId, order.items.map { it.productOptionId })
        } catch (e: Exception) {
            log.error("[PAYMENT_POST_PROCESS] Error processing for paymentId: {}, orderKey: {}", paymentId, orderKey, e)
        }
    }
}
