package com.example.monolithic.init;

import com.example.monolithic.point.domain.Point;
import com.example.monolithic.point.infrastructure.PointRepository;
import com.example.monolithic.product.domain.Product;
import com.example.monolithic.product.infrastructure.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TestDataCreator {

    private final PointRepository pointRepository;

    private final ProductRepository productRepository;

    public TestDataCreator(PointRepository pointRepository, ProductRepository productRepository) {
        this.pointRepository = pointRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void createTestData() {
        pointRepository.save(new Point(1L, 10000L));

        productRepository.save(new Product(100L, 100L));
        productRepository.save(new Product(100L, 200L));
    }
}
