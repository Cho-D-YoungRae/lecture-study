package org.example.payment.application.service;

import org.example.payment.adapter.out.persistent.repository.PaymentEventEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentEventJpaRepository;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderJpaRepository;
import org.example.payment.application.port.in.CheckoutCommand;
import org.example.payment.application.port.in.CheckoutUseCase;
import org.example.payment.domain.CheckoutResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CheckoutServiceTest {

    @Autowired
    private CheckoutUseCase checkoutUseCase;

    @Autowired
    private PaymentEventJpaRepository paymentEventJpaRepository;

    @Autowired
    private PaymentOrderJpaRepository paymentOrderJpaRepository;

    @Test
    void should_save_PaymentEvent_and_PaymentOrder_successfully() {
        String orderId = UUID.randomUUID().toString();
        CheckoutCommand checkoutCommand = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L, 3L))
                .idempotencyKey(orderId)
                .build();

        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);

        assertThat(checkoutResult.amount()).isEqualTo(60000);
        assertThat(checkoutResult.orderId()).isEqualTo(orderId);

        PaymentEventEntity paymentEventEntity = paymentEventJpaRepository.findByOrderId(orderId).get();
        assertThat(paymentEventEntity.getOrderId()).isEqualTo(orderId);
        assertThat(paymentEventEntity.getPaymentDone()).isFalse();

        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderJpaRepository
                .findAllByPaymentEvent(paymentEventEntity);
        assertThat(paymentOrderEntityList).hasSize(checkoutCommand.productIds().size());
        assertThat(paymentOrderEntityList).allMatch(entity -> entity.getLedgerUpdated() == false);
        assertThat(paymentOrderEntityList).allMatch(entity -> entity.getWalletUpdated() == false);
    }

    @Test
    void show_fail_to_save_PaymentEvent_and_PaymentOrder_when_trying_to_save_for_the_second_time() {
        String orderId = UUID.randomUUID().toString();
        CheckoutCommand checkoutCommand = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L, 3L))
                .idempotencyKey(orderId)
                .build();
        checkoutUseCase.checkout(checkoutCommand);

        assertThatThrownBy(() -> checkoutUseCase.checkout(checkoutCommand))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}