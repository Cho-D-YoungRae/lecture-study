package com.example.product.web;

import com.example.product.application.ProductFacadeService;
import com.example.product.application.RedisLockService;
import com.example.product.application.dto.ProductReserveResult;
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
        String key = "product:" + request.requestId();
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
}
