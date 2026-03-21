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

/**
 * 현재는 CartItemManager 를 직접 참고하고 있지만 오염이 커진다고 느껴지면(복잡도가 커지면)
 * processList: List<PaymentPostProcess> 와 같은 형태로 주입받아서 순회하며 로직처리 할 수도 있다.
 * > 아니면 의존성을 느슨하게 하기 위해 이벤트를 고려해볼 수도 있다.
 */
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
