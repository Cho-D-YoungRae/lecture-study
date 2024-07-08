package org.example.payment.adapter.out.web.toss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Configuration
public class TossWebClientConfiguration {

    @Bean
    public RestClient tossPaymentRestClient(
            final RestClient.Builder restClientBuilder,
            @Value("${psp.toss.url}") final String baseUrl,
            @Value("${psp.toss.secret-key}") final String secretKey
    ) {
        final String encodedSecretKey = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());
        return restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
