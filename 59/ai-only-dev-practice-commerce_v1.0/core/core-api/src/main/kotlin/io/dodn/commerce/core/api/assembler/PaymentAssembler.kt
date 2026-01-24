package io.dodn.commerce.core.api.assembler

import io.dodn.commerce.core.api.controller.v1.request.CreatePaymentByInviteRequest
import io.dodn.commerce.core.api.controller.v1.request.CreatePaymentRequest
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.OwnedCouponService
import io.dodn.commerce.core.domain.PaymentDiscount
import io.dodn.commerce.core.domain.PaymentService
import io.dodn.commerce.core.domain.PointBalance
import io.dodn.commerce.core.domain.PointService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.OrderState
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PaymentAssembler(
    private val paymentService: PaymentService,
    private val orderService: OrderService,
    private val ownedCouponService: OwnedCouponService,
    private val pointService: PointService,
) {
    fun create(user: User, request: CreatePaymentRequest): Long {
        val order = orderService.getOrder(user, request.orderKey, OrderState.CREATED)
        val ownedCoupons = ownedCouponService.getOwnedCouponsForCheckout(user, order.items.map { it.productId })
        val pointBalance = pointService.balance(user)

        return paymentService.createPayment(
            order = order,
            paymentDiscount = request.toPaymentDiscount(ownedCoupons, pointBalance),
            payerId = user.id,
        )
    }

    fun createByInvite(user: User, request: CreatePaymentByInviteRequest): Long {
        val order = orderService.getOrderByInviteKey(request.inviteKey)

        return paymentService.createPayment(
            order = order,
            paymentDiscount = PaymentDiscount(
                ownedCoupons = emptyList(),
                pointBalance = PointBalance(user.id, BigDecimal.ZERO),
                useOwnedCouponId = -1,
                usePointAmount = BigDecimal.valueOf(-1),
            ),
            payerId = user.id,
        )
    }
}
