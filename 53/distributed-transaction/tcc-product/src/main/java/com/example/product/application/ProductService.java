package com.example.product.application;

import com.example.product.application.dto.ProductReserveCommand;
import com.example.product.application.dto.ProductReserveConfirmCommand;
import com.example.product.application.dto.ProductReserveResult;
import com.example.product.domain.Product;
import com.example.product.domain.ProductReservation;
import com.example.product.infrastructure.ProductRepository;
import com.example.product.infrastructure.ProductReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;

    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        List<ProductReservation> reservations = productReservationRepository.findAllByRequestId(command.requestId());
        if (!reservations.isEmpty()) {
            long totalPrice = reservations.stream().mapToLong(ProductReservation::getReservedPrice).sum();
            return new ProductReserveResult(totalPrice);
        }

        Long totalPrice = 0L;
        for (ProductReserveCommand.ReserveItem item : command.items()) {
            Product product = productRepository.findById(item.productId()).orElseThrow();

            Long price = product.reserve(item.reserveQuantity());
            totalPrice += price;
            productRepository.save(product);
            productReservationRepository.save(
                    new ProductReservation(
                            command.requestId(),
                            item.productId(),
                            item.reserveQuantity(),
                            price
                    )
            );
        }

        return new ProductReserveResult(totalPrice);
    }

    @Transactional
    public void confirmReserve(ProductReserveConfirmCommand command) {
        List<ProductReservation> reservations = productReservationRepository.findAllByRequestId(command.requestId());

        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("예약 정보가 존재하지 않습니다.");
        }

        boolean alreadyConfirmed = reservations.stream()
                .anyMatch(item -> item.getStatus() == ProductReservation.ProductReservationStatus.CONFIRMED);

        if (alreadyConfirmed) {
            log.info("이미 예약이 확정되었습니다. requestId: {}", command.requestId());
            return;
        }

        for (ProductReservation reservation : reservations) {
            Product product = productRepository.findById(reservation.getProductId()).orElseThrow();

            product.confirm(reservation.getReservedQuantity());
            reservation.confirm();

            productRepository.save(product);
            productReservationRepository.save(reservation);
        }
    }
}
