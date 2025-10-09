package com.example.product.web.dto;

import com.example.product.application.dto.ProductReserveConfirmCommand;

public record ProductReserveConfirmRequest(String requestId) {

    public ProductReserveConfirmCommand toCommand() {
        return new ProductReserveConfirmCommand(requestId);
    }
}
