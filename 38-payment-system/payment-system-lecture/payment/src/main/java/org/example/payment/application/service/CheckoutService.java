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

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    private final LoadProductPort loadProductPort;
    private final SavePaymentPort savePaymentPort;

    @Override
    public CheckoutResult checkout(CheckoutCommand command) {
        List<Product> products = loadProductPort.getProducts(command.cartId(), command.productIds());
        PaymentEvent paymentEvent = createPaymentEvent(command, products);
        savePaymentPort.save(paymentEvent);
        return new CheckoutResult(paymentEvent.totalAmount(), paymentEvent.orderId(), paymentEvent.orderName());
    }

    private PaymentEvent createPaymentEvent(CheckoutCommand command, List<Product> products) {
        return PaymentEvent.builder()
                .buyerId(command.buyerId())
                .orderId(command.idempotencyKey())
                .orderName(products.stream().map(Product::name).collect(Collectors.joining()))
                .paymentOrders(products.stream().map(product -> new PaymentOrder(
                        product.sellerId(),
                        product.id(),
                        command.idempotencyKey(),
                        product.amount()
                )).toList())
                .build();
    }
}
