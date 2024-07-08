package org.example.payment.adapter.out.persistent;

import lombok.RequiredArgsConstructor;
import org.example.common.PersistentAdapter;
import org.example.payment.adapter.out.persistent.repository.PaymentEventEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentEventJpaRepository;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderJpaRepository;
import org.example.payment.application.port.out.SavePaymentPort;
import org.example.payment.domain.PaymentEvent;
import org.springframework.transaction.annotation.Transactional;

@PersistentAdapter
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort {

    private final PaymentEventJpaRepository paymentRepository;

    private final PaymentOrderJpaRepository paymentOrderRepository;

    @Override
    @Transactional
    public long save(final PaymentEvent paymentEvent) {
        final PaymentEventEntity paymentEventEntity = paymentRepository.save(PaymentEventEntity.from(paymentEvent));
        paymentOrderRepository.saveAll(
                paymentEvent.paymentOrders().stream()
                        .map(order -> new PaymentOrderEntity(
                                paymentEvent.id(),
                                paymentEventEntity,
                                order.sellerId(),
                                order.productId(),
                                order.orderId(),
                                order.amount(),
                                order.paymentStatus(),
                                order.ledgerUpdated(),
                                order.walletUpdated(),
                                null,
                                null
                        )).toList()
        );
        return paymentEventEntity.getId();
    }
}
