package com.library.feign

import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NaverClientConfiguration {

    @Bean
    fun requestInterceptor(
        @Value("\${external.naver.headers.client-id}") clientId: String,
        @Value("\${external.naver.headers.client-secret}") clientSecret: String
    ): RequestInterceptor {
        return RequestInterceptor { requestTemplate ->
            requestTemplate
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
        }
    }

    @Bean
    fun errorDecoder(objectMapper: ObjectMapper): NaverErrorDecoder {
        return NaverErrorDecoder(objectMapper)
    }
}