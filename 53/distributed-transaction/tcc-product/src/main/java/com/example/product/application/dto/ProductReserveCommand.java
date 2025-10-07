package com.example.product.application.dto;

import java.util.List;

public record ProductReserveCommand(
        String requestId,
       List<ReserveItem> items
) {

    public record ReserveItem(
            Long productId,
            Long reserveQuantity
    ) {
    }
}
