package tobyspring.hellospring;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tobyspring.hellospring.api.ApiTemplate;
import tobyspring.hellospring.exrate.CachedExRateProvider;
import tobyspring.hellospring.exrate.RestTemplateExRateProvider;
import tobyspring.hellospring.payment.ExRateProvider;
import tobyspring.hellospring.payment.PaymentService;

import java.time.Clock;

@Configuration
@ComponentScan
public class PaymentConfig {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService(cachedExRateProvider(), clock());
    }

    @Bean
    public ExRateProvider exRateProvider() {
        return new RestTemplateExRateProvider(restTemplate());
    }

    @Bean
    public ExRateProvider cachedExRateProvider() {
        return new CachedExRateProvider(exRateProvider());
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ApiTemplate apiTemplate() {
        return new ApiTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
