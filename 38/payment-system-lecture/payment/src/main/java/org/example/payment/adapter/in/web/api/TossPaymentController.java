package org.example.payment.adapter.in.web.api;

import lombok.RequiredArgsConstructor;
import org.example.common.WebAdapter;
import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.application.port.in.PaymentConfirmUseCase;
import org.example.payment.domain.PaymentConfirmationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class TossPaymentController {

    private final PaymentConfirmUseCase paymentConfirmUseCase;

    @PostMapping("/v1/toss/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmationResult>> confirm(@RequestBody TossPaymentConfirmRequest request) {
        PaymentConfirmCommand command = new PaymentConfirmCommand(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );

        return ResponseEntity.ok(
                ApiResponse.success(paymentConfirmUseCase.confirm(command))
        );
    }
}
