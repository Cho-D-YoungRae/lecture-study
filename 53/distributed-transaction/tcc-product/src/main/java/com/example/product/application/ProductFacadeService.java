package com.example.product.application;

import com.example.product.application.dto.ProductReserveCommand;
import com.example.product.application.dto.ProductReserveConfirmCommand;
import com.example.product.application.dto.ProductReserveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacadeService {

    private final ProductService productService;

    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                return productService.tryReserve(command);
            } catch (ObjectOptimisticLockingFailureException e) {
                tryCount++;
            }
        }

        throw new IllegalStateException("예약에 실패하였습니다.");
    }

    public void confirmReserve(ProductReserveConfirmCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                productService.confirmReserve(command);
            } catch (ObjectOptimisticLockingFailureException e) {
                tryCount++;
            }
        }

        throw new IllegalStateException("예약 확정에 실패하였습니다.");
    }
}
