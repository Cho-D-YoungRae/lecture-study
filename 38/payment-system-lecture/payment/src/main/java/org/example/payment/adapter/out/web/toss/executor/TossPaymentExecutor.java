package org.example.payment.adapter.out.web.toss.executor;

import lombok.RequiredArgsConstructor;
import org.example.payment.adapter.out.web.toss.exception.PSPConfirmationException;
import org.example.payment.adapter.out.web.toss.exception.TossPaymentError;
import org.example.payment.adapter.out.web.toss.response.TossPaymentConfirmationResponse;
import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.domain.PSPConfirmationStatus;
import org.example.payment.domain.PaymentExecutionResult;
import org.example.payment.domain.PaymentMethod;
import org.example.payment.domain.PaymentType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.NoOpResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TossPaymentExecutor implements PaymentExecutor {

    private static final String URI = "/v1/payments/confirm";

    private final RestClient tossPaymentRestClient;

    // retry 로직 강의와 다름 -> 실제로 사용하면 내게 맞게 수정해야할듯
    // 로그도 남기고... 등등
    @Override
    @Retryable(
            retryFor = PSPConfirmationException.class,
            maxAttempts = 2
    )
    public PaymentExecutionResult execute(PaymentConfirmCommand command) {
        TossPaymentConfirmationResponse response = Objects.requireNonNull(
                tossPaymentRestClient.post()
                        .uri(URI)
                        .header("Idempotency-Key", command.orderId())
                        .body(command)
                        .retrieve()
                        .onStatus(new NoOpResponseErrorHandler())
                        .body(TossPaymentConfirmationResponse.class)
        );

        if (response.failure() != null) {
            TossPaymentConfirmationResponse.TossFailureResponse failure = response.failure();
            TossPaymentError tossPaymentError = TossPaymentError.get(failure.code());
            throw new PSPConfirmationException(
                    tossPaymentError.name(),
                    tossPaymentError.getDescription(),
                    tossPaymentError.isSuccess(),
                    tossPaymentError.isFailure(),
                    tossPaymentError.isUnknown(),
                    tossPaymentError.isRetryableError()
            );
        }

        return new PaymentExecutionResult(
                response.paymentKey(),
                response.orderId(),
                new PaymentExecutionResult.PaymentExtraDetails(
                        PaymentType.get(response.type()),
                        PaymentMethod.get(response.method()),
                        LocalDateTime.parse(response.approvedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        response.orderName(),
                        PSPConfirmationStatus.get(response.status()),
                        response.totalAmount(),
                        response.toString()
                ),
                null,
                true,
                false,
                false,
                false
        );
    }
}
