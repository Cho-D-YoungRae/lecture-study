package com.example.point.application;

import com.example.point.domain.Point;
import com.example.point.domain.PointReservation;
import com.example.point.application.dto.PointReserveCommand;
import com.example.point.infrastructure.PointRepository;
import com.example.point.infrastructure.PointReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService {

    private final PointRepository pointRepository;
    private final PointReservationRepository pointReservationRepository;

    @Transactional
    public void tryReserve(PointReserveCommand command) {
        pointReservationRepository.findByRequestId(command.requestId())
                .ifPresentOrElse(pointReservation -> {
                    log.info("이미 처리된 요청입니다. requestId={}", command.requestId());
                }, () -> {
                    Point point = pointRepository.findByUserId(command.userId()).orElseThrow();
                    point.reserve(command.reserveAmount());
                    pointReservationRepository.save(
                            new PointReservation(command.requestId(), point.getId(), command.reserveAmount())
                    );
                });
    }
}
