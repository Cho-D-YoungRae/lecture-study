package jpabook.jpashop.api;

import jpabook.jpashop.common.Result;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> result = orderRepository.findAllByString(new OrderSearch());

        // LazyLoading 강제 초기화
        for (Order order : result) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().forEach(oi -> oi.getItem().getName());
        }
        return result;
    }

    @GetMapping("/api/v2/orders")
    public Result<List<OrderDto>> ordersV2() {
        List<OrderDto> result = orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderDto::new)
                .collect(toList());

        return Result.<List<OrderDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @GetMapping("/api/v3/orders")
    public Result<List<OrderDto>> ordersV3() {
        List<OrderDto> result = orderRepository.findAllWithItem().stream()
                .map(OrderDto::new)
                .collect(toList());

        return Result.<List<OrderDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @GetMapping("/api/v3.1/orders")
    public Result<List<OrderDto>> ordersV3Paging(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int limit) {
        List<OrderDto> result = orderRepository.findAllWithMemberDelivery(offset, limit).stream()
                .map(OrderDto::new)
                .collect(toList());

        return Result.<List<OrderDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @GetMapping("/api/v4/orders")
    public Result<List<OrderQueryDto>> ordersV4() {
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos();
        return Result.<List<OrderQueryDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @GetMapping("/api/v5/orders")
    public Result<List<OrderQueryDto>> ordersV5() {
        List<OrderQueryDto> result = orderQueryRepository.findAllByDto_optimization();
        return Result.<List<OrderQueryDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @GetMapping("/api/v6/orders")
    public Result<List<OrderQueryDto>> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> result = flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(
                                o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(
                        e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(),
                        e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());

        return Result.<List<OrderQueryDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @Data
    static class OrderDto {

        private Long orderId;

        private String name;

        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

        private Address address;

        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;    // 상품명

        private int orderPrice;     // 주문 가격

        private int count;          // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
