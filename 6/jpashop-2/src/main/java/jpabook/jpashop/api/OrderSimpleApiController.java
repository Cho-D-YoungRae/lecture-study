package jpabook.jpashop.api;

import jpabook.jpashop.common.Result;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne (ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderRepository.findAllByString(new OrderSearch());
    }

    // N + 1 문제 발생
    @GetMapping("/api/v2/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV2() {
        List<SimpleOrderDto> result = orderRepository.findAllByString(new OrderSearch()).stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return Result.<List<SimpleOrderDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    // 패치조인으로 N + 1 문제 해결
    // 불필요한 필드 값들도 모두 조회됨
    @GetMapping("/api/v3/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV3() {
        List<SimpleOrderDto> result = orderRepository.findAllWithMemberDelivery().stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return Result.<List<SimpleOrderDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    // JPA 에서 DTO 로 바로 조회 -> 불필요한 필드 값 조회되는 문제 해결
    @GetMapping("/api/v4/simple-orders")
    public Result<List<OrderSimpleQueryDto>> ordersV4() {
        List<OrderSimpleQueryDto> result = orderSimpleQueryRepository .findOrderDtos();

        return Result.<List<OrderSimpleQueryDto>>builder()
                .success(true)
                .data(result)
                .build();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
