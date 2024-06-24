package org.example.payment.adapter.out.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Base64;

@Configuration
public class TossWebClientConfiguration {

    @Bean
    public WebClient tossPaymentWebClient(
            @Value("${psp.toss.url}") final String baseUrl,
            @Value("${psp.toss.secret-key}") final String secretKey
    ) {
        final String encodedSecretKey = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(reactorClientHttpConnector())
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
    }

    private ClientHttpConnector reactorClientHttpConnector() {
        final ConnectionProvider provider = ConnectionProvider.builder("toss-payment")
                .build();
        return new ReactorClientHttpConnector(HttpClient.create(provider));
    }
}
