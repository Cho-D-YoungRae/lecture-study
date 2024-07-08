package org.example.payment.adapter.out.web.product;

import lombok.RequiredArgsConstructor;
import org.example.common.WebAdapter;
import org.example.payment.adapter.out.web.product.client.ProductClient;
import org.example.payment.application.port.out.LoadProductPort;
import org.example.payment.domain.Product;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ProductWebAdapter implements LoadProductPort {

    private final ProductClient productClient;

    @Override
    public List<Product> getProducts(final long cartId, final List<Long> productIds) {
        return productClient.getProducts(cartId, productIds);
    }
}
