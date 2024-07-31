package tobyspring.hellospring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.hellospring.payment.Payment;
import tobyspring.hellospring.payment.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ContextConfiguration(classes = TestPaymentConfig.class)
class PaymentServiceSpringTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Clock clock;

    @Test
    void prepare() throws IOException {

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        assertThat(payment.getExRate()).isEqualByComparingTo(BigDecimal.valueOf(1_000));
        assertThat(payment.getConvertedAmount()).isEqualTo(BigDecimal.valueOf(10_000));
    }

    @Test
    void validUntil() throws IOException {
        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        // valid untile 이 prepare() 30분 뒤로 설정됐는가?
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }

}