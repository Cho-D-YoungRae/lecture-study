package org.example.payment.adapter.out.persistent;

import lombok.RequiredArgsConstructor;
import org.example.common.PersistentAdapter;
import org.example.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException;
import org.example.payment.adapter.out.persistent.exception.PaymentValidationException;
import org.example.payment.adapter.out.persistent.repository.PaymentEventEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentEventEntityJpaRepository;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderEntityJpaRepository;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderHistoryEntity;
import org.example.payment.adapter.out.persistent.repository.PaymentOrderHistoryEntityJpaRepository;
import org.example.payment.application.port.out.PaymentStatusUpdateCommand;
import org.example.payment.application.port.out.PaymentStatusUpdatePort;
import org.example.payment.application.port.out.PaymentValidationPort;
import org.example.payment.application.port.out.SavePaymentPort;
import org.example.payment.domain.PaymentEvent;
import org.example.payment.domain.PaymentExecutionResult;
import org.example.payment.domain.PaymentStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@PersistentAdapter
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort {

    private final PaymentEventEntityJpaRepository paymentRepository;
    private final PaymentOrderEntityJpaRepository paymentOrderRepository;
    private final PaymentOrderHistoryEntityJpaRepository paymentOrderHistoryRepository;


    @Override
    @Transactional
    public long save(PaymentEvent paymentEvent) {
        PaymentEventEntity paymentEventEntity = paymentRepository.save(PaymentEventEntity.from(paymentEvent));
        paymentOrderRepository.saveAll(
                paymentEvent.paymentOrders().stream()
                        .map(order -> new PaymentOrderEntity(
                                paymentEvent.id(),
                                paymentEventEntity,
                                order.sellerId(),
                                order.productId(),
                                order.orderId(),
                                order.amount(),
                                order.paymentStatus(),
                                order.ledgerUpdated(),
                                order.walletUpdated(),
                                null,
                                null
                        )).toList()
        );
        return paymentEventEntity.getId();
    }

    @Override
    @Transactional
    public void updatePaymentStatusToExecuting(String orderId, String paymentKey) {
        PaymentEventEntity paymentEventEntity = paymentRepository.findByOrderId(orderId).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderRepository
                .findAllByPaymentEvent(paymentEventEntity);
        if (paymentOrderEntityList.isEmpty()) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        checkPreviousPaymentOrderStatus(paymentOrderEntityList);
        insertPaymentHistory(paymentOrderEntityList, PaymentStatus.EXECUTING, "PAYMENT_CONFIRMATION_START");
        updatePaymentOrderStatus(paymentOrderEntityList, PaymentStatus.EXECUTING);
        updatePaymentKey(paymentEventEntity, paymentKey);
    }

    private void checkPreviousPaymentOrderStatus(List<PaymentOrderEntity> paymentOrderEntityList) {
        paymentOrderEntityList.stream()
                .filter(entity -> entity.getStatus() == PaymentStatus.SUCCESS)
                .findAny()
                .ifPresent((entity) -> {
                    throw new PaymentAlreadyProcessedException(PaymentStatus.SUCCESS, "이미 처리 성공한 결제입니다.");
                });

        paymentOrderEntityList.stream()
                .filter(entity -> entity.getStatus() == PaymentStatus.FAILURE)
                .findAny()
                .ifPresent((entity) -> {
                    throw new PaymentAlreadyProcessedException(PaymentStatus.FAILURE, "이미 처리 실패한 결제입니다.");
                });
    }

    private void insertPaymentHistory(
            List<PaymentOrderEntity> paymentOrderEntityList, PaymentStatus status, String reason
    ) {
        paymentOrderHistoryRepository.saveAll(
                paymentOrderEntityList.stream()
                        .map(paymentOrderEntity -> PaymentOrderHistoryEntity.builder()
                                .paymentOrderId(paymentOrderEntity.getId())
                                .previousStatus(paymentOrderEntity.getStatus())
                                .newStatus(status)
                                .reason(reason)
                                .build()
                        ).toList()
        );
    }

    private void updatePaymentOrderStatus(List<PaymentOrderEntity> paymentOrderEntityList, PaymentStatus status) {
        paymentOrderEntityList.forEach(paymentOrderEntity -> paymentOrderEntity.setStatus(status));
    }

    private void updatePaymentKey(PaymentEventEntity paymentEventEntity, String paymentKey) {
        paymentEventEntity.setPaymentKey(paymentKey);
    }

    @Override
    public void validate(String orderId, long amount) {
        PaymentEventEntity paymentEventEntity = paymentRepository.findByOrderId(orderId).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderRepository
                .findAllByPaymentEvent(paymentEventEntity);
        if (paymentOrderEntityList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        long totalAmount = paymentOrderEntityList.stream()
                .mapToLong(PaymentOrderEntity::getAmount)
                .sum();

        if (totalAmount != amount) {
            throw new PaymentValidationException(
                    "결제(orderId:" + orderId + ") 에서 금액(amount:" + amount + ")이 올바르지 않습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePaymentStatus(PaymentStatusUpdateCommand command) {
        if (PaymentStatus.SUCCESS == command.status()) {
            updatePaymentStatusToSuccess(command);
        } else if (PaymentStatus.FAILURE == command.status()) {
            updatePaymentStatusToFailure(command);
        } else if (PaymentStatus.UNKNOWN == command.status()) {
            updatePaymentStatusToUnknown(command);
        } else {
            throw new IllegalStateException("결제 상태 (status: " + command.status() + ") 는 올바르지 않은 결제 상태 입니다.");
        }
    }

    private void updatePaymentStatusToSuccess(PaymentStatusUpdateCommand command) {
        PaymentEventEntity paymentEventEntity = paymentRepository.findByOrderId(command.orderId()).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderRepository
                .findAllByPaymentEvent(paymentEventEntity);
        if (paymentOrderEntityList.isEmpty()) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        insertPaymentHistory(paymentOrderEntityList, command.status(), "PAYMENT_CONFIRMATION_DONE");
        updatePaymentOrderStatus(paymentOrderEntityList, command.status());
        updatePaymentEventExtraDetails(paymentEventEntity, command);
    }

    private void updatePaymentEventExtraDetails(PaymentEventEntity paymentEventEntity, PaymentStatusUpdateCommand command) {
        PaymentExecutionResult.PaymentExtraDetails extraDetails = Objects.requireNonNull(command.extraDetails());
        paymentEventEntity.setOrderName(extraDetails.orderName());
        paymentEventEntity.setMethod(extraDetails.method());
        paymentEventEntity.setApprovedAt(extraDetails.approvedAt());
        paymentEventEntity.setOrderId(command.orderId());
        paymentEventEntity.setType(extraDetails.type());
        paymentEventEntity.setPspRawData(extraDetails.pspRawData());
    }

    private void updatePaymentStatusToFailure(PaymentStatusUpdateCommand command) {
        PaymentEventEntity paymentEventEntity = paymentRepository.findByOrderId(command.orderId()).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderRepository
                .findAllByPaymentEvent(paymentEventEntity);
        if (paymentOrderEntityList.isEmpty()) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        insertPaymentHistory(paymentOrderEntityList,
                command.status(), Objects.requireNonNull(command.failure()).toString());
        updatePaymentOrderStatus(paymentOrderEntityList, command.status());
    }

    private void updatePaymentStatusToUnknown(PaymentStatusUpdateCommand command) {
        PaymentEventEntity paymentEventEntity = paymentRepository.findByOrderId(command.orderId()).orElseThrow();
        List<PaymentOrderEntity> paymentOrderEntityList = paymentOrderRepository
                .findAllByPaymentEvent(paymentEventEntity);
        if (paymentOrderEntityList.isEmpty()) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        insertPaymentHistory(paymentOrderEntityList, command.status(), "UNKNOWN");
        updatePaymentOrderStatus(paymentOrderEntityList, command.status());
        // 다른 상태 업데이트와 달리 실패 카운트를 증가시킴
        incrementPaymentOrderFailedCount(paymentOrderEntityList);
    }

    private void incrementPaymentOrderFailedCount(List<PaymentOrderEntity> paymentOrderEntityList) {
        paymentOrderEntityList.forEach(paymentOrderEntity -> {
            int failedCount = paymentOrderEntity.getFailedCount();
            paymentOrderEntity.setFailedCount(failedCount + 1);
        });
    }
}
