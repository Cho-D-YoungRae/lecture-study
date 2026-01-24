package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.TransactionType
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class PaymentService(
    private val paymentCreator: PaymentCreator,
    private val paymentManager: PaymentManager,
    private val transactionHistoryAppender: TransactionHistoryAppender,
    private val paymentPostProcessor: PaymentPostProcessor,
) {
    fun createPayment(order: Order, paymentDiscount: PaymentDiscount, payerId: Long): Long {
        return paymentCreator.create(order, paymentDiscount, payerId)
    }

    fun success(orderKey: String, externalPaymentKey: String, amount: BigDecimal): Long {
        paymentManager.validateForApprove(orderKey, amount)

        /**
         * NOTE: PG 승인 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         */

        val payment = paymentManager.success(orderKey, externalPaymentKey)

        transactionHistoryAppender.append(
            type = TransactionType.PAYMENT,
            userId = payment.userId,
            orderId = payment.orderId,
            paymentId = payment.id,
            externalPaymentKey = externalPaymentKey,
            paidAmount = payment.paidAmount,
            message = "결제 성공",
            occurredAt = payment.paidAt!!,
        )

        paymentPostProcessor.process(payment.userId, orderKey)

        return payment.id
    }

    fun fail(orderKey: String, code: String, message: String) {
        val payment = paymentManager.validateForFail(orderKey)

        transactionHistoryAppender.append(
            type = TransactionType.PAYMENT_FAIL,
            userId = payment.userId,
            orderId = payment.orderId,
            paymentId = payment.id,
            externalPaymentKey = "",
            paidAmount = BigDecimal.valueOf(-1),
            message = "[$code] $message",
            occurredAt = LocalDateTime.now(),
        )
    }
}
