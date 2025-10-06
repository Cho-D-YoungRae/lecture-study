package com.example.monolithic.order.application;

import com.example.monolithic.order.application.dto.CreateOrderCommand;
import com.example.monolithic.order.application.dto.CreateOrderResult;
import com.example.monolithic.order.application.dto.PlaceOrderCommand;
import com.example.monolithic.order.domain.Order;
import com.example.monolithic.order.domain.OrderItem;
import com.example.monolithic.order.infrastructure.OrderItemRepository;
import com.example.monolithic.order.infrastructure.OrderRepository;
import com.example.monolithic.point.application.PointService;
import com.example.monolithic.product.application.ProductService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class OrderService {

    private static final Logger log = getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointService pointService;
    private final ProductService productService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            PointService pointService,
            ProductService productService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.pointService = pointService;
        this.productService = productService;
    }

    @Transactional
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order());
        List<OrderItem> orderItems = command.orderItems().stream()
                .map(item -> new OrderItem(order.getId(), item.productId(), item.quantity()))
                .toList();
        orderItemRepository.saveAll(orderItems);
        return new CreateOrderResult(order.getId());
    }

    @Transactional
    public void placeOrder(PlaceOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));
        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            return;
        }

        Long totalPrice = 0L;
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        for (OrderItem item : orderItems) {
            totalPrice += productService.buy(item.getProductId(), item.getQuantity());
        }

        // userId 임시
        pointService.use(1L, totalPrice);

        order.complete();
        orderRepository.save(order);

        log.info("결제 완료!!");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
