package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CancelValidator(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val orderItemRepository: OrderItemRepository,
) {
    fun validate(user: User, action: CancelAction) {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(action.orderKey, OrderState.PAID, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (order.userId != user.id) {
            throw CoreException(ErrorType.NOT_FOUND_DATA)
        }

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (payment.state != PaymentState.SUCCESS) {
            throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        }
    }

    fun validatePartial(user: User, action: PartialCancelAction) {
        val order = orderRepository.findByOrderKeyAndStatus(action.orderKey, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (order.userId != user.id) {
            throw CoreException(ErrorType.NOT_FOUND_DATA)
        }

        if (order.state != OrderState.PAID && order.state != OrderState.PARTIAL_CANCELED) {
            throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        }

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (payment.state != PaymentState.SUCCESS) {
            throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        }

        val targetItem = orderItemRepository.findByIdOrNull(action.orderItemId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (targetItem.orderId != order.id) {
            throw CoreException(ErrorType.NOT_FOUND_DATA)
        }

        if (targetItem.cancellableQuantity() < action.quantity) {
            throw CoreException(ErrorType.INVALID_REQUEST)
        }
    }
}
