package com.example.product.web.dto;

import com.example.product.application.dto.ProductReserveCommand;

import java.util.List;

public record ProductReserveRequest(
        String requestId,
        List<ReserveItem> items
) {

    public ProductReserveCommand toProductReserveCommand() {
        return new ProductReserveCommand(
                requestId,
                items.stream().map(item ->
                        new ProductReserveCommand.ReserveItem(item.productId, item.reserveQuantity)
                ).toList()
        );
    }

    public record ReserveItem(
            Long productId,
            Long reserveQuantity
    ) {
    }
}
