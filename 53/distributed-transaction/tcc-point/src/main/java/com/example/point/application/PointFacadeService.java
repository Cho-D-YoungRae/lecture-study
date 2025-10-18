package com.example.point.application;

import com.example.point.application.dto.PointReserveCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointFacadeService {

    private final PointService pointService;

    @Transactional
    public void tryReserve(PointReserveCommand command) {
        int tryCount = 0;
        while (tryCount < 3) {
            try {
                pointService.tryReserve(command);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                tryCount++;
            }
        }

        throw new IllegalStateException("예약 실패");
    }
}
