package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.assembler.PaymentAssembler
import io.dodn.commerce.core.api.controller.v1.request.CreateInvitePaymentRequest
import io.dodn.commerce.core.api.controller.v1.request.CreatePaymentRequest
import io.dodn.commerce.core.api.controller.v1.response.CreatePaymentResponse
import io.dodn.commerce.core.domain.PaymentService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class PaymentController(
    private val paymentService: PaymentService,
    private val paymentAssembler: PaymentAssembler,
) {
    @PostMapping("/v1/payments")
    fun create(
        user: User,
        @RequestBody request: CreatePaymentRequest,
    ): ApiResponse<CreatePaymentResponse> {
        val id = paymentAssembler.create(user, request)
        return ApiResponse.success(CreatePaymentResponse(id))
    }

    @PostMapping("/v1/payments/invite")
    fun createByInvite(
        user: User,
        @RequestBody request: CreateInvitePaymentRequest,
    ): ApiResponse<CreatePaymentResponse> {
        val id = paymentAssembler.createByInvite(user, request)
        return ApiResponse.success(CreatePaymentResponse(id))
    }

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
