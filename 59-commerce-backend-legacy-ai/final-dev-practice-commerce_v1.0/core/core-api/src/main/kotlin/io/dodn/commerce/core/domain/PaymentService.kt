package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PaymentService(
    private val paymentCreator: PaymentCreator,
    private val paymentProcessor: PaymentProcessor,
    private val paymentValidator: PaymentValidator,
    private val paymentPostProcessor: PaymentPostProcessor,
) {
    fun createPayment(order: Order, payer: User, paymentDiscount: PaymentDiscount): Long {
        return paymentCreator.create(order, payer, paymentDiscount)
    }

    fun success(orderKey: String, externalPaymentKey: String, amount: BigDecimal): Long {
        paymentValidator.validateForApprove(orderKey, amount)

        /**
         * NOTE: PG 승인 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         */

        val paymentId = paymentProcessor.success(orderKey, externalPaymentKey)
        paymentPostProcessor.process(paymentId, orderKey)
        return paymentId
    }

    fun fail(orderKey: String, code: String, message: String) {
        paymentValidator.validateForFail(orderKey)
        paymentProcessor.fail(orderKey, code, message)
    }
}
