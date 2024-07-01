package org.example.payment.adapter.out.web.product.client;

import org.example.payment.domain.Product;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProductClient {

    Flux<Product> getProducts(long cartId, List<Long> productIds);
}
