package com.example.point.controller;

import com.example.point.application.PointFacadeService;
import com.example.point.application.RedisLockService;
import com.example.point.controller.dto.PointReserveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointFacadeService pointFacadeService;
    private final RedisLockService redisLockService;

    @PostMapping("/point/reserve")
    public void reserve(@RequestBody PointReserveRequest request) {
        String key = "point:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new IllegalStateException("락 획득 실패");
        }

        try {
            pointFacadeService.tryReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
