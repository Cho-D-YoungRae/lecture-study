package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentCreator(
    private val paymentRepository: PaymentRepository,
) {
    @Transactional
    fun create(
        order: Order,
        payer: User,
        paymentDiscount: PaymentDiscount,
    ): Long {
        if (paymentRepository.findByOrderId(order.id)?.state == PaymentState.SUCCESS) throw CoreException(ErrorType.ORDER_ALREADY_PAID)

        val payment = PaymentEntity(
            userId = order.userId,
            orderId = order.id,
            originAmount = order.totalPrice,
            ownedCouponId = paymentDiscount.useOwnedCouponId,
            couponDiscount = paymentDiscount.couponDiscount,
            usedPoint = paymentDiscount.usePoint,
            payerUserId = payer.id,
            paidAmount = paymentDiscount.paidAmount(order.totalPrice),
            state = PaymentState.READY,
        )
        return paymentRepository.save(payment).id
    }
}
