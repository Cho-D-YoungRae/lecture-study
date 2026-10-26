package com.example.point.infrastructure;

import com.example.point.domain.PointReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointReservationRepository extends JpaRepository<PointReservation, Long> {

    Optional<PointReservation> findByRequestId(String requestId);
}
