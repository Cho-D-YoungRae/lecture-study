package tobyspring.hellospring.exrate;

import tobyspring.hellospring.payment.ExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class SimpleExRateProvider implements ExRateProvider {

    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        if ("USD".equals(currency)) {
            return BigDecimal.valueOf(1000);
        }

        throw new IllegalArgumentException("지원하지 않는 통화입니다.");
    }
}
