package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.storage.db.core.CancelEntity
import io.dodn.commerce.storage.db.core.CancelRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SettlementTargetReader(
    private val paymentRepository: PaymentRepository,
    private val cancelRepository: CancelRepository,
) {
    fun readPaymentsByStateAndPaidAtBetween(
        state: PaymentState,
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable,
    ): Slice<SettlementPayment> {
        return paymentRepository.findAllByStateAndPaidAtBetween(state, from, to, pageable)
            .map { it.toConcept() }
    }

    fun readCancelsByCanceledAtBetween(
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable,
    ): Slice<SettlementCancel> {
        return cancelRepository.findAllByCanceledAtBetween(from, to, pageable)
            .map { it.toConcept() }
    }

    private fun PaymentEntity.toConcept(): SettlementPayment {
        return SettlementPayment(
            id = id,
            userId = userId,
            orderId = orderId,
            originAmount = originAmount,
            ownedCouponId = ownedCouponId,
            couponDiscount = couponDiscount,
            usedPoint = usedPoint,
            payerUserId = payerUserId,
            paidAmount = paidAmount,
            state = state,
            externalPaymentKey = externalPaymentKey,
            method = method,
            approveCode = approveCode,
            paidAt = paidAt,
        )
    }

    private fun CancelEntity.toConcept(): SettlementCancel {
        return SettlementCancel(
            id = id,
            userId = userId,
            type = type,
            orderId = orderId,
            orderItemId = orderItemId ?: -1L,
            paymentId = paymentId,
            originAmount = originAmount,
            ownedCouponId = ownedCouponId,
            couponDiscount = couponDiscount,
            usedPoint = usedPoint,
            paidAmount = paidAmount,
            canceledQuantity = canceledQuantity ?: -1L,
            canceledPaidAmount = canceledPaidAmount,
            canceledPointAmount = canceledPointAmount,
            canceledCouponAmount = canceledCouponAmount,
            externalCancelKey = externalCancelKey,
            canceledAt = canceledAt,
        )
    }
}
