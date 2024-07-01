package org.example.payment.adapter.out.persistent.repository;

import lombok.RequiredArgsConstructor;
import org.example.payment.domain.PaymentEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class R2DBCPaymentRepository implements PaymentRepository {


    private final DatabaseClient databaseClient;

    private final TransactionalOperator transactionalOperator;
    @Override
    public Mono<Void> save(final PaymentEvent paymentEvent) {
        return insertPaymentEvent(paymentEvent)
                .flatMap(it -> selectPaymentEventId())
                .flatMap(paymentEventId -> insertPaymentOrders(paymentEvent, paymentEventId))
                .as(transactionalOperator::transactional)
                .then();
    }

    private Mono<Long> insertPaymentEvent(final PaymentEvent paymentEvent) {
        return databaseClient.sql(INSERT_PAYMENT_EVENT_QUERY)
                .bind("buyerId", paymentEvent.buyerId())
                .bind("orderName", paymentEvent.orderName())
                .bind("orderId", paymentEvent.orderId())
                .fetch()
                .rowsUpdated();
    }

    private Mono<Long> selectPaymentEventId() {
        return databaseClient.sql(LAST_INSERT_ID_QUERY)
                .fetch()
                .first()
                .map(result -> (Long) result.get("last_insert_id"));
    }

    private Mono<Long> insertPaymentOrders(final PaymentEvent paymentEvent, final long paymentEventId) {
        final String insertSql = INSERT_PAYMENT_ORDER_QUERY + " " + paymentEvent.paymentOrders().stream()
                .map(paymentOrder -> "(" +
                        paymentEventId + ", " +
                        paymentOrder.sellerId() + ", " +
                        paymentOrder.orderId() + ", " +
                        paymentOrder.productId() + ", " +
                        paymentOrder.amount() + ", " +
                        paymentOrder.paymentStatus() +
                        ")"
                ).collect(Collectors.joining(", "));

        return databaseClient.sql(insertSql)
                .fetch()
                .rowsUpdated();
    }

    private static final String INSERT_PAYMENT_EVENT_QUERY = """
            insert into payment_event (buyer_id, order_name, order_id)
            values (:buyerId, :orderName, :orderId)
            """.trim();

    private static final String LAST_INSERT_ID_QUERY = """
            select last_insert_id()
            """.trim();

    private static final String INSERT_PAYMENT_ORDER_QUERY = """
            insert into payment_orders (payment_event_id, seller_id, product_id, amount, payment_order_status)
            """.trim();

}
