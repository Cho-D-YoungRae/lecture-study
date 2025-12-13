package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.CreatePaymentRequest
import io.dodn.commerce.core.api.controller.v1.response.CreatePaymentResponse
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.OwnedCouponService
import io.dodn.commerce.core.domain.PaymentService
import io.dodn.commerce.core.domain.PointService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class PaymentController(
    private val paymentService: PaymentService,
    private val orderService: OrderService,
    private val ownedCouponService: OwnedCouponService,
    private val pointService: PointService,
) {
    @PostMapping("/v1/payments")
    fun create(
        user: User,
        @RequestBody request: CreatePaymentRequest,
    ): ApiResponse<CreatePaymentResponse> {
        val order = orderService.getOrder(user, request.orderKey, OrderState.CREATED)
        val ownedCoupons = ownedCouponService.getOwnedCouponsForCheckout(user, order.items.map { it.productId })
        val pointBalance = pointService.balance(user)
        val id = paymentService.createPayment(
            order = order,
            paymentDiscount = request.toPaymentDiscount(ownedCoupons, pointBalance),
        )
        return ApiResponse.success(CreatePaymentResponse(id))
    }

    /**
     * PG 사에서 결제 성공 콜백을 받는 엔드포인트
     * PG 사에서 호출하는 것이기 때문에 RequestParam 은 PG 사의 스펙에 따름
     */
    @PostMapping("/v1/payments/callback/success")
    fun callbackForSuccess(
        @RequestParam orderId: String,
        @RequestParam paymentKey: String,
        @RequestParam amount: BigDecimal,
    ): ApiResponse<Any> {
        paymentService.success(
            orderKey = orderId,
            externalPaymentKey = paymentKey,
            amount = amount,
        )
        return ApiResponse.success()
    }

    /**
     * PG 사에서 결제 실패 콜백을 받는 엔드포인트
     */
    @PostMapping("/v1/payments/callback/fail")
    fun callbackForFail(
        @RequestParam orderId: String,
        @RequestParam code: String,
        @RequestParam message: String,
    ): ApiResponse<Any> {
        paymentService.fail(
            orderKey = orderId,
            code = code,
            message = message,
        )
        return ApiResponse.success()
    }
}
