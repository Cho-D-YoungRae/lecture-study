package com.example.product.web;

import com.example.product.application.ProductFacadeService;
import com.example.product.application.RedisLockService;
import com.example.product.application.dto.ProductReserveResult;
import com.example.product.web.dto.ProductReserveCancelRequest;
import com.example.product.web.dto.ProductReserveConfirmRequest;
import com.example.product.web.dto.ProductReserveRequest;
import com.example.product.web.dto.ProductReserveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacadeService productService;
    private final RedisLockService redisLockService;

    @PostMapping("/product/reserve")
    public ProductReserveResponse reserve(@RequestBody ProductReserveRequest request) {
        String key = lockKey(request.requestId());
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new IllegalStateException("락 획에 실패했습니다.");
        }

        try {
            ProductReserveResult result = productService.tryReserve(request.toProductReserveCommand());
            return new ProductReserveResponse(result.totalPrice());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    @PostMapping("/product/confirm")
    public void confirm(@RequestBody ProductReserveConfirmRequest request) {
        String key = lockKey(request.requestId());
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new IllegalStateException("락 획에 실패했습니다.");
        }

        try {
            productService.confirmReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    @PostMapping("/product/cancel")
    public void cancel(@RequestBody ProductReserveCancelRequest request) {
        String key = lockKey(request.requestId());
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new IllegalStateException("락 획에 실패했습니다.");
        }

        try {
            productService.cancelReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    private String lockKey(String requestId) {
        return "product:" + requestId;
    }
}
