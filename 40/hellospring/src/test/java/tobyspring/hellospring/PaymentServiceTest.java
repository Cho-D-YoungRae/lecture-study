package tobyspring.hellospring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tobyspring.hellospring.payment.Payment;
import tobyspring.hellospring.payment.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    Clock clock;

    @BeforeEach
    void beforeEach() {
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }
    @Test
    void convertedAmount() {

        testAmount(BigDecimal.valueOf(500), BigDecimal.valueOf(5000));
        testAmount(BigDecimal.valueOf(1_000), BigDecimal.valueOf(10_000));
        testAmount(BigDecimal.valueOf(3_000), BigDecimal.valueOf(30_000));
    }

    @Test
    void validUntil() {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(BigDecimal.valueOf(1_000)), this.clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        // valid untile 이 prepare() 30분 뒤로 설정됐는가?
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }


    private void testAmount(BigDecimal exRate, BigDecimal convertedAmount) {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(exRate), this.clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        assertThat(payment.getExRate()).isEqualByComparingTo(exRate);
        assertThat(payment.getConvertedAmount())
                .isEqualTo(convertedAmount);
    }
}