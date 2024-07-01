package org.example.payment.application.port.in;

import org.example.payment.domain.CheckoutResult;
import reactor.core.publisher.Mono;

public interface CheckoutUseCase {

    Mono<CheckoutResult> checkout(CheckoutCommand command);
}
