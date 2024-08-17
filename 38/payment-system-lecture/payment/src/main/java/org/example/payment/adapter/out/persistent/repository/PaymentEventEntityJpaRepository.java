package org.example.payment.adapter.out.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentEventEntityJpaRepository extends JpaRepository<PaymentEventEntity, Long> {

    Optional<PaymentEventEntity> findByOrderId(String orderId);
}
