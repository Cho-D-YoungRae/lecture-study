package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.assembler.OrderAssembler
import io.dodn.commerce.core.api.controller.v1.request.CreateOrderFromCartRequest
import io.dodn.commerce.core.api.controller.v1.request.CreateOrderRequest
import io.dodn.commerce.core.api.controller.v1.response.CreateOrderResponse
import io.dodn.commerce.core.api.controller.v1.response.OrderCheckoutResponse
import io.dodn.commerce.core.api.controller.v1.response.OrderListResponse
import io.dodn.commerce.core.api.controller.v1.response.OrderResponse
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderService: OrderService,
    private val orderAssembler: OrderAssembler,
) {
    @PostMapping("/v1/orders")
    fun create(
        user: User,
        @RequestBody request: CreateOrderRequest,
    ): ApiResponse<CreateOrderResponse> {
        val key = orderService.create(
            user = user,
            newOrder = request.toNewOrder(user),
        )
        return ApiResponse.success(
            CreateOrderResponse(
                orderKey = key,
            ),
        )
    }

    @PostMapping("/v1/cart-orders")
    fun createFromCart(
        user: User,
        @RequestBody request: CreateOrderFromCartRequest,
    ): ApiResponse<CreateOrderResponse> {
        val key = orderAssembler.createFromCart(user, request)
        return ApiResponse.success(
            CreateOrderResponse(
                orderKey = key,
            ),
        )
    }

    @GetMapping("/v1/orders/{orderKey}/checkout")
    fun findOrderForCheckout(
        user: User,
        @PathVariable orderKey: String,
    ): ApiResponse<OrderCheckoutResponse> {
        return ApiResponse.success(orderAssembler.findOrderForCheckout(user, orderKey))
    }

    @GetMapping("/v1/orders")
    fun getOrders(user: User): ApiResponse<List<OrderListResponse>> {
        val orders = orderService.getOrders(user)
        return ApiResponse.success(OrderListResponse.of(orders))
    }

    @GetMapping("/v1/orders/{orderKey}")
    fun getOrder(
        user: User,
        @PathVariable orderKey: String,
    ): ApiResponse<OrderResponse> {
        val order = orderService.getOrder(user, orderKey, OrderState.PAID)
        return ApiResponse.success(OrderResponse.of(order))
    }

    @PostMapping("/v1/orders/{orderKey}/invite")
    fun createInvite(
        user: User,
        @PathVariable orderKey: String,
    ): ApiResponse<CreateOrderResponse> {
        val inviteKey = orderService.createInvite(user, orderKey)
        return ApiResponse.success(CreateOrderResponse(orderKey = inviteKey))
    }

    @GetMapping("/v1/order-invites/{inviteKey}")
    fun getOrderByInviteKey(
        @PathVariable inviteKey: String,
    ): ApiResponse<OrderResponse> {
        val order = orderService.getOrderByInviteKey(inviteKey)
        return ApiResponse.success(OrderResponse.of(order))
    }
}
