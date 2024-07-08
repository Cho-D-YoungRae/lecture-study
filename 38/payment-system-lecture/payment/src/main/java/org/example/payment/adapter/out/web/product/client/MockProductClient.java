package org.example.payment.adapter.out.web.product.client;

import org.example.payment.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockProductClient implements ProductClient {

    @Override
    public List<Product> getProducts(final long cartId, final List<Long> productIds) {
        return productIds.stream()
                .map(id -> new Product(
                        id,
                        10000 * id,
                        2,
                        "test_product_" + id,
                        1
                )).toList();
    }
}
