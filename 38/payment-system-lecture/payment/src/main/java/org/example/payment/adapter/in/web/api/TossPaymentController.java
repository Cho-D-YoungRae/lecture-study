package org.example.payment.adapter.in.web.api;

import lombok.RequiredArgsConstructor;
import org.example.common.WebAdapter;
import org.example.payment.adapter.out.web.executor.TossPaymentExecution;
import org.example.payment.adapter.out.web.executor.TossPaymentExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class TossPaymentController {

    private final TossPaymentExecutor tossPaymentExecutor;

    @PostMapping("/v1/toss/confirm")
    public Mono<ResponseEntity<ApiResponse<String>>> confirm(@RequestBody final TossPaymentConfirmRequest request) {
        return tossPaymentExecutor.execute(new TossPaymentExecution(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        ))
                .map( ApiResponse::success)
                .map(ResponseEntity::ok);
    }
}
