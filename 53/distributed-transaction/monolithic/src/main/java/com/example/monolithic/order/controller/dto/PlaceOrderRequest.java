package com.example.monolithic.order.controller.dto;

import com.example.monolithic.order.application.dto.PlaceOrderCommand;

import java.util.List;

public record PlaceOrderRequest(
        List<OrderItem> orderItems
) {
    
    public PlaceOrderCommand toPlaceOrderCommand() {
        return new PlaceOrderCommand(orderItems.stream()
                .map(item -> new PlaceOrderCommand.OrderItem(item.productId(), item.quantity()))
                .toList());
    }

    public record OrderItem(Long productId, Long quantity) {}
}
