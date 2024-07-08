package org.example.payment.adapter.out.persistent;

import lombok.RequiredArgsConstructor;
import org.example.common.PersistentAdapter;
import org.example.payment.adapter.out.persistent.repository.PaymentEventEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentJpaRepository;
import org.example.payment.application.port.out.SavePaymentPort;
import org.example.payment.domain.PaymentEvent;

@PersistentAdapter
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort {

    private final PaymentJpaRepository paymentRepository;

    @Override
    public long save(final PaymentEvent paymentEvent) {
        return paymentRepository.save(PaymentEventEntity.from(paymentEvent)).getId();
    }
}
