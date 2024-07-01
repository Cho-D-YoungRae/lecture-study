package org.example.payment.adapter.out.web.product;

import org.example.common.WebAdapter;
import org.example.payment.application.port.out.LoadProductPort;
import org.example.payment.domain.Product;
import reactor.core.publisher.Flux;

import java.util.List;

@WebAdapter
public class ProductWebAdapter implements LoadProductPort {

    @Override
    public Flux<Product> getProducts(final long cartId, final List<Long> productIds) {
        return null;
    }
}
