package org.example.payment.application.port.out;

import org.example.payment.domain.Product;
import reactor.core.publisher.Flux;

import java.util.List;

public interface LoadProductPort {

    Flux<Product> getProducts(long cartId, List<Long> productIds);
}
