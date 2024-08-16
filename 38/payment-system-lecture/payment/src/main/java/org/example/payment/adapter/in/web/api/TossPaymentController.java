package org.example.payment.adapter.in.web.api;

import lombok.RequiredArgsConstructor;
import org.example.common.WebAdapter;
import org.example.payment.adapter.out.web.toss.executor.TossPaymentExecution;
import org.example.payment.adapter.out.web.toss.executor.TossPaymentExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class TossPaymentController {

    private final TossPaymentExecutor tossPaymentExecutor;

    @PostMapping("/v1/toss/confirm")
    public ResponseEntity<ApiResponse<String>> confirm(@RequestBody TossPaymentConfirmRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                tossPaymentExecutor.execute(new TossPaymentExecution(
                        request.paymentKey(),
                        request.orderId(),
                        request.amount()
                ))
        ));
    }
}
