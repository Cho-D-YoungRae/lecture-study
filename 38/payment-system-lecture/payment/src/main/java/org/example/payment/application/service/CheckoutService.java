package org.example.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.example.common.UseCase;
import org.example.payment.application.port.in.CheckoutCommand;
import org.example.payment.application.port.in.CheckoutUseCase;
import org.example.payment.application.port.out.LoadProductPort;
import org.example.payment.application.port.out.SavePaymentPort;
import org.example.payment.domain.CheckoutResult;
import org.example.payment.domain.PaymentEvent;
import org.example.payment.domain.PaymentOrder;
import org.example.payment.domain.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@Service
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    private final LoadProductPort loadProductPort;
    private final SavePaymentPort savePaymentPort;

    @Override
    public Mono<CheckoutResult> checkout(final CheckoutCommand command) {
        return loadProductPort.getProducts(command.cartId(), command.productIds())
                .collectList()
                .map(product -> createPaymentEvent(command, product))
                .flatMap(paymentEvent -> savePaymentPort.save(paymentEvent).thenReturn(paymentEvent))
                .map(paymentEvent -> new CheckoutResult(paymentEvent.totalAmount(), paymentEvent.orderId(), paymentEvent.orderName()));
    }

    private PaymentEvent createPaymentEvent(final CheckoutCommand command, List<Product> products) {
        return new PaymentEvent(
                command.buyerId(),
                products.stream().map(Product::name).collect(Collectors.joining()),
                command.idempotencyKey(),
                products.stream().map(product -> new PaymentOrder(
                        product.sellerId(),
                        product.id(),
                        command.idempotencyKey(),
                        product.amount()
                )).toList()
        );
    }
}
