package org.example.payment.adapter.out.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEventEntity, Long> {
}
