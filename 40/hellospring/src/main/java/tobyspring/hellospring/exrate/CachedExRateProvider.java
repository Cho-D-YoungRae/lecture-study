package tobyspring.hellospring.exrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tobyspring.hellospring.payment.ExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CachedExRateProvider implements ExRateProvider {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ExRateProvider target;

    private BigDecimal cachedExRate;
    private LocalDateTime cachedExpiryTime;

    public CachedExRateProvider(ExRateProvider target) {
        this.target = target;
    }

    @Override
    public BigDecimal getExRate(String currency) {
        if (cachedExRate == null || cachedExpiryTime.isBefore(LocalDateTime.now())) {
            cachedExRate = target.getExRate(currency);
            cachedExpiryTime = LocalDateTime.now().plusSeconds(3);
            log.info("Cache Updated : {}", cachedExRate);
        }
        return cachedExRate;
    }
}
