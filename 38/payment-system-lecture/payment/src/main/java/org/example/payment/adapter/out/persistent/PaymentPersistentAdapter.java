package org.example.payment.adapter.out.persistent;

import lombok.RequiredArgsConstructor;
import org.example.common.PersistentAdapter;
import org.example.payment.adapter.out.persistent.repository.PaymentRepository;
import org.example.payment.application.port.out.SavePaymentPort;
import org.example.payment.domain.PaymentEvent;
import reactor.core.publisher.Mono;

@PersistentAdapter
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort {

    private final PaymentRepository paymentRepository;

    @Override
    public Mono<Void> save(final PaymentEvent paymentEvent) {
        return paymentRepository.save(paymentEvent);
    }
}
