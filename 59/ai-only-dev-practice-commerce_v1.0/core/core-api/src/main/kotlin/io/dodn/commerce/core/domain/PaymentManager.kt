package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class PaymentManager(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val pointHandler: PointHandler,
    private val ownedCouponUsageManager: OwnedCouponUsageManager,
) {
    @Transactional
    fun success(orderKey: String, externalPaymentKey: String): Payment {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val paymentEntity = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        paymentEntity.success(
            externalPaymentKey,
            PaymentMethod.CARD,
            "PG 승인 API 호출의 응답 값 중 `승인번호` 넣기",
        )
        order.paid()

        if (paymentEntity.hasAppliedCoupon()) {
            ownedCouponUsageManager.useOne(paymentEntity.ownedCouponId)
        }

        pointHandler.deduct(User(paymentEntity.userId), PointType.PAYMENT, paymentEntity.id, paymentEntity.usedPoint)
        pointHandler.earn(User(paymentEntity.userId), PointType.PAYMENT, paymentEntity.id, PointAmount.PAYMENT)

        return Payment(
            id = paymentEntity.id,
            userId = paymentEntity.userId,
            orderId = paymentEntity.orderId,
            originAmount = paymentEntity.originAmount,
            ownedCouponId = paymentEntity.ownedCouponId,
            couponDiscount = paymentEntity.couponDiscount,
            usedPoint = paymentEntity.usedPoint,
            paidAmount = paymentEntity.paidAmount,
            state = paymentEntity.state,
            externalPaymentKey = paymentEntity.externalPaymentKey,
            method = paymentEntity.method,
            approveCode = paymentEntity.approveCode,
            paidAt = paymentEntity.paidAt,
        )
    }

    fun validateForApprove(orderKey: String, amount: BigDecimal) {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.state != PaymentState.READY) throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        if (payment.paidAmount != amount) throw CoreException(ErrorType.PAYMENT_AMOUNT_MISMATCH)
    }

    fun validateForFail(orderKey: String): Payment {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val paymentEntity = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        return Payment(
            id = paymentEntity.id,
            userId = paymentEntity.userId,
            orderId = paymentEntity.orderId,
            originAmount = paymentEntity.originAmount,
            ownedCouponId = paymentEntity.ownedCouponId,
            couponDiscount = paymentEntity.couponDiscount,
            usedPoint = paymentEntity.usedPoint,
            paidAmount = paymentEntity.paidAmount,
            state = paymentEntity.state,
            externalPaymentKey = paymentEntity.externalPaymentKey,
            method = paymentEntity.method,
            approveCode = paymentEntity.approveCode,
            paidAt = paymentEntity.paidAt,
        )
    }
}
