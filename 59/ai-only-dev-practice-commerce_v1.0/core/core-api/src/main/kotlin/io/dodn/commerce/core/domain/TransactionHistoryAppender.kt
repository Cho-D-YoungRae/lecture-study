package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.core.TransactionHistoryEntity
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class TransactionHistoryAppender(
    private val transactionHistoryRepository: TransactionHistoryRepository,
) {
    @Transactional
    fun append(
        type: TransactionType,
        userId: Long,
        orderId: Long,
        paymentId: Long,
        externalPaymentKey: String,
        paidAmount: BigDecimal,
        pointAmount: BigDecimal = BigDecimal.ZERO,
        couponAmount: BigDecimal = BigDecimal.ZERO,
        message: String,
        occurredAt: LocalDateTime,
    ) {
        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = type,
                userId = userId,
                orderId = orderId,
                paymentId = paymentId,
                externalPaymentKey = externalPaymentKey,
                paidAmount = paidAmount,
                pointAmount = pointAmount,
                couponAmount = couponAmount,
                message = message,
                occurredAt = occurredAt,
            ),
        )
    }
}
