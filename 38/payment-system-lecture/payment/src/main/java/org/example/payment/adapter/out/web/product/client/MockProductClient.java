package org.example.payment.adapter.out.web.product.client;

import org.example.payment.domain.Product;
import reactor.core.publisher.Flux;

import java.util.List;

public class MockProductClient implements ProductClient {

    @Override
    public Flux<Product> getProducts(final long cartId, final List<Long> productIds) {
        return Flux.fromIterable(productIds.stream()
                .map(id ->
                        new Product(
                                id,
                                10000 * id,
                                2,
                                "test_product_" + id,
                                1
                        )
                ).toList()
        );
    }
}
