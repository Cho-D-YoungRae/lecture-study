package org.example.payment.adapter.out.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderEntityJpaRepository extends JpaRepository<PaymentOrderEntity, Long> {

    List<PaymentOrderEntity> findAllByPaymentEvent(PaymentEventEntity paymentEvent);
}
