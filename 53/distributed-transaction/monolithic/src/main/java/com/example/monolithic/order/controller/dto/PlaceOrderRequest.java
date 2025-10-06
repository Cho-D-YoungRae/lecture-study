package com.example.monolithic.order.controller.dto;

import com.example.monolithic.order.application.dto.PlaceOrderCommand;

public record PlaceOrderRequest(
        Long orderId
) {

    public PlaceOrderCommand toPlaceOrderCommand() {
        return new PlaceOrderCommand(orderId);
    }
}
