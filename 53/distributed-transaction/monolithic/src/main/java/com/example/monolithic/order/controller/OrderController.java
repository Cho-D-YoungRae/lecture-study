package com.example.monolithic.order.controller;

import com.example.monolithic.order.application.OrderService;
import com.example.monolithic.order.application.dto.CreateOrderResult;
import com.example.monolithic.order.controller.dto.CreateOrderRequest;
import com.example.monolithic.order.controller.dto.CreateOrderResponse;
import com.example.monolithic.order.controller.dto.PlaceOrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResult result = orderService.createOrder(request.toCreateOrderCommand());
        return new CreateOrderResponse(result.orderId());
    }

    @PostMapping("/orders/place")
    public void placeOrder(@RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request.toPlaceOrderCommand());
    }
}
