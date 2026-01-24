package io.dodn.commerce.core.api.assembler

import io.dodn.commerce.core.api.controller.v1.request.CreateOrderFromCartRequest
import io.dodn.commerce.core.api.controller.v1.response.OrderCheckoutResponse
import io.dodn.commerce.core.domain.CartService
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.OwnedCouponService
import io.dodn.commerce.core.domain.PointService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.OrderState
import org.springframework.stereotype.Component

@Component
class OrderAssembler(
    private val orderService: OrderService,
    private val cartService: CartService,
    private val ownedCouponService: OwnedCouponService,
    private val pointService: PointService,
) {
    fun createFromCart(user: User, request: CreateOrderFromCartRequest): String {
        val cart = cartService.getCart(user)
        return orderService.create(
            user = user,
            newOrder = cart.toNewOrder(request.cartItemIds),
        )
    }

    fun findOrderForCheckout(user: User, orderKey: String): OrderCheckoutResponse {
        val order = orderService.getOrder(user, orderKey, OrderState.CREATED)
        val ownedCoupons = ownedCouponService.getOwnedCouponsForCheckout(user, order.items.map { it.productId })
        val pointBalance = pointService.balance(user)
        return OrderCheckoutResponse.of(order, ownedCoupons, pointBalance)
    }
}
