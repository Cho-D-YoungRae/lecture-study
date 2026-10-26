package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.storage.db.core.CancelRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SettlementSourceReader(
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
            .map {
                SettlementPayment(
                    id = it.id,
                    userId = it.userId,
                    orderId = it.orderId,
                    originAmount = it.originAmount,
                    ownedCouponId = it.ownedCouponId,
                    couponDiscount = it.couponDiscount,
                    usedPoint = it.usedPoint,
                    payerUserId = it.payerUserId,
                    paidAmount = it.paidAmount,
                    state = it.state,
                    externalPaymentKey = it.externalPaymentKey,
                    method = it.method,
                    approveCode = it.approveCode,
                    paidAt = it.paidAt,
                )
            }
    }

    fun readCancelsByCanceledAtBetween(
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable,
    ): Slice<SettlementCancel> {
        return cancelRepository.findAllByCanceledAtBetween(from, to, pageable)
            .map {
                SettlementCancel(
                    id = it.id,
                    userId = it.userId,
                    type = it.type,
                    orderId = it.orderId,
                    orderItemId = it.orderItemId,
                    paymentId = it.paymentId,
                    originAmount = it.originAmount,
                    ownedCouponId = it.ownedCouponId,
                    couponDiscount = it.couponDiscount,
                    usedPoint = it.usedPoint,
                    paidAmount = it.paidAmount,
                    canceledQuantity = it.canceledQuantity,
                    canceledPaidAmount = it.canceledPaidAmount,
                    canceledPointAmount = it.canceledPointAmount,
                    canceledCouponAmount = it.canceledCouponAmount,
                    externalCancelKey = it.externalCancelKey,
                    canceledAt = it.canceledAt,
                )
            }
    }
}
