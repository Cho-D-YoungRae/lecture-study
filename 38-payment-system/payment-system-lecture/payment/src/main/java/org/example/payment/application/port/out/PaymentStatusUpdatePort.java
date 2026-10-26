package org.example.payment.application.port.out;

public interface PaymentStatusUpdatePort {

    /**
     *  초기 단계에서 상태를 EXECUTING 으로 변경하기 때문에
     *  페이먼트 서비스는 처리 중에 문제가 발생해도 작업을 복구할 수 있는 기반을 마련할 수 있음
     */
    void updatePaymentStatusToExecuting(String orderId, String paymentKey);

    void updatePaymentStatus(PaymentStatusUpdateCommand command);
}
