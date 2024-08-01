package tobyspring.hellospring.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import tobyspring.hellospring.OrderConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringJUnitConfig(classes = OrderConfig.class)
class OrderServiceSpringTest {

    @Autowired
    OrderService orderService;

    @Autowired
    private DataSource dataSource;

    @Test
    void createOrder() {
        var order = orderService.createOrder("0100", BigDecimal.ONE);

        assertThat(order.getId()).isPositive();
    }

    @Test
    void createOrders() {
        var reqs = List.of(
                new OrderReq("0100", BigDecimal.ONE),
                new OrderReq("0200", BigDecimal.TEN)
        );

        var orders = orderService.createOrders(reqs);

        assertThat(orders).hasSize(2);
        orders.forEach(order -> assertThat(order.getId()).isPositive());
    }

    @Test
    void createDuplicatedOrders() {
        var reqs = List.of(
                new OrderReq("0300", BigDecimal.ONE),
                new OrderReq("0300", BigDecimal.TEN)
        );

        assertThatThrownBy(() -> orderService.createOrders(reqs))
                .isInstanceOf(DataIntegrityViolationException.class);

        var jdbcClient = JdbcClient.create(dataSource);
        var count = jdbcClient.sql("select count(*) from orders")
                .query(Long.class)
                .single();
        assertThat(count).isZero();
    }
}
