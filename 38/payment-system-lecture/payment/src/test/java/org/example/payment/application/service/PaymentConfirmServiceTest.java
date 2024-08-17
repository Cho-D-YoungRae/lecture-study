package org.example.payment.application.service;

import org.example.payment.adapter.out.persistent.repository.PaymentEventEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentEventEntityJpaRepository;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntityJpaRepository;
import org.example.payment.application.port.in.CheckoutCommand;
import org.example.payment.application.port.in.CheckoutUseCase;
import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.application.port.out.PaymentExecutorPort;
import org.example.payment.application.port.out.PaymentStatusUpdatePort;
import org.example.payment.application.port.out.PaymentValidationPort;
import org.example.payment.domain.CheckoutResult;
import org.example.payment.domain.PSPConfirmationStatus;
import org.example.payment.domain.PaymentConfirmationResult;
import org.example.payment.domain.PaymentExecutionResult;
import org.example.payment.domain.PaymentMethod;
import org.example.payment.domain.PaymentStatus;
import org.example.payment.domain.PaymentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentConfirmServiceTest {

    @Autowired
    private CheckoutUseCase checkoutUseCase;

    @Autowired
    private PaymentStatusUpdatePort paymentStatusUpdatePort;

    @Autowired
    private PaymentValidationPort paymentValidationPort;

    @MockBean
    private PaymentExecutorPort mockPaymentExecutorPort;

    @Autowired
    private PaymentEventEntityJpaRepository paymentEventEntityJpaRepository;

    @Autowired
    private PaymentOrderEntityJpaRepository paymentOrderEntityJpaRepository;

    @Test
    void should_be_marked_as_SUCCESS_if_Payment_Confirmation_success_in_PSP() {
        // given
        String orderId = UUID.randomUUID().toString();

        CheckoutCommand checkoutCommand = new CheckoutCommand(1L, 1L, List.of(1L, 2L, 3L), orderId);
        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);

        PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(
                UUID.randomUUID().toString(),
                orderId,
                checkoutResult.amount()
        );

        PaymentConfirmService paymentConfirmService = new PaymentConfirmService(
                paymentStatusUpdatePort,
                paymentValidationPort,
                mockPaymentExecutorPort
        );

        PaymentExecutionResult paymentExecutionResult = new PaymentExecutionResult(
                paymentConfirmCommand.paymentKey(),
                paymentConfirmCommand.orderId(),
                new PaymentExecutionResult.PaymentExtraDetails(
                        PaymentType.NORMAL,
                        PaymentMethod.EAZY_PAY,
                        LocalDateTime.now(),
                        "test order name",
                        PSPConfirmationStatus.DONE,
                        paymentConfirmCommand.amount(),
                        "{}"
                ),
                null,
                true,
                false,
                false,
                false
        );

        given(mockPaymentExecutorPort.execute(paymentConfirmCommand)).willReturn(paymentExecutionResult);

        // when
        PaymentConfirmationResult paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand);

        // then

        PaymentEventEntity paymentEventEntity = paymentEventEntityJpaRepository.findByOrderId(orderId).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntities = paymentOrderEntityJpaRepository.findAllByPaymentEvent(paymentEventEntity);
        assertThat(paymentConfirmationResult.status()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(
                paymentOrderEntities.stream()
                        .allMatch(paymentOrderEntity -> paymentOrderEntity.getStatus() == PaymentStatus.SUCCESS)
        ).isTrue();
        assertThat(
                paymentEventEntity.getType()
        ).isEqualTo(paymentExecutionResult.extraDetails().type());
        assertThat(
                paymentEventEntity.getMethod()
        ).isEqualTo(paymentExecutionResult.extraDetails().method());
        assertThat(
                paymentEventEntity.getApprovedAt()
        ).isEqualTo(paymentExecutionResult.extraDetails().approvedAt());
        assertThat(
                paymentEventEntity.getOrderName()
        ).isEqualTo(paymentExecutionResult.extraDetails().orderName());

    }

    @Test
    void should_be_marked_as_FAILURE_if_Payment_Confirmation_fails_on_PSP() {
        // given
        String orderId = UUID.randomUUID().toString();

        CheckoutCommand checkoutCommand = new CheckoutCommand(1L, 1L, List.of(1L, 2L, 3L), orderId);
        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);

        PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(
                UUID.randomUUID().toString(),
                orderId,
                checkoutResult.amount()
        );

        PaymentConfirmService paymentConfirmService = new PaymentConfirmService(
                paymentStatusUpdatePort,
                paymentValidationPort,
                mockPaymentExecutorPort
        );

        PaymentExecutionResult paymentExecutionResult = new PaymentExecutionResult(
                paymentConfirmCommand.paymentKey(),
                paymentConfirmCommand.orderId(),
                null,
                new PaymentExecutionResult.PaymentExecutionFailure(
                        "ERROR",
                        "test error message"
                ),
                false,
                true,
                false,
                false
        );

        given(mockPaymentExecutorPort.execute(paymentConfirmCommand)).willReturn(paymentExecutionResult);

        // when
        PaymentConfirmationResult paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand);

        // then

        PaymentEventEntity paymentEventEntity = paymentEventEntityJpaRepository.findByOrderId(orderId).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntities = paymentOrderEntityJpaRepository.findAllByPaymentEvent(paymentEventEntity);
        assertThat(paymentConfirmationResult.status()).isEqualTo(PaymentStatus.FAILURE);
        assertThat(
                paymentOrderEntities.stream()
                        .allMatch(paymentOrderEntity -> paymentOrderEntity.getStatus() == PaymentStatus.FAILURE)
        ).isTrue();

    }

    @Test
    void should_be_marked_as_UNKNOWN_if_Payment_Confirmation_fails_due_to_an_unknown_exception() {
        // given
        String orderId = UUID.randomUUID().toString();

        CheckoutCommand checkoutCommand = new CheckoutCommand(1L, 1L, List.of(1L, 2L, 3L), orderId);
        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);

        PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(
                UUID.randomUUID().toString(),
                orderId,
                checkoutResult.amount()
        );

        PaymentConfirmService paymentConfirmService = new PaymentConfirmService(
                paymentStatusUpdatePort,
                paymentValidationPort,
                mockPaymentExecutorPort
        );

        PaymentExecutionResult paymentExecutionResult = new PaymentExecutionResult(
                paymentConfirmCommand.paymentKey(),
                paymentConfirmCommand.orderId(),
                null,
                new PaymentExecutionResult.PaymentExecutionFailure(
                        "ERROR",
                        "test error message"
                ),
                false,
                false,
                true,
                false
        );

        given(mockPaymentExecutorPort.execute(paymentConfirmCommand)).willReturn(paymentExecutionResult);

        // when
        PaymentConfirmationResult paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand);

        // then
        PaymentEventEntity paymentEventEntity = paymentEventEntityJpaRepository.findByOrderId(orderId).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntities = paymentOrderEntityJpaRepository.findAllByPaymentEvent(paymentEventEntity);
        assertThat(paymentConfirmationResult.status()).isEqualTo(PaymentStatus.UNKNOWN);
        assertThat(
                paymentOrderEntities.stream()
                        .allMatch(paymentOrderEntity -> paymentOrderEntity.getStatus() == PaymentStatus.UNKNOWN)
        ).isTrue();

    }
}