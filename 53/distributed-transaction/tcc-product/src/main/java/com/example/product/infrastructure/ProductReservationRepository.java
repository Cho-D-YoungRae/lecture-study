package com.example.product.infrastructure;

import com.example.product.domain.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, Long> {

    List<ProductReservation> findAllByRequestId(String requestId);
}
