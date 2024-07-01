package org.example.payment.adapter.out.persistent.repository;

import org.example.payment.domain.PaymentEvent;
import reactor.core.publisher.Mono;

public interface PaymentRepository {

    Mono<Void> save(PaymentEvent paymentEvent);
}
