package com.library.feign

import feign.RequestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NaverClientConfigurationTest {

    lateinit var configuration: NaverClientConfiguration

    @BeforeEach
    fun setUp() {
        configuration = NaverClientConfiguration()
    }

    @Test
    fun `requestInterceptor의 header에 key값들이 적용된다`() {
        // given
        val template = RequestTemplate()
        val clientId = "testClientId"
        val clientSecret = "testClientSecret"

        // and: interceptor 를 타기전에 header가 존재하지 않는다
        assertThat(template.headers()).isEmpty()
        assertThat(template.headers()["X-Naver-Client-Id"]).isNull()
        assertThat(template.headers()["X-Naver-Client-Secret"]).isNull()

        // when: intercepter를 탄다
        val interceptor = configuration.requestInterceptor(clientId, clientSecret)
        interceptor.apply(template)

        // then
        assertThat(template.headers()["X-Naver-Client-Id"])
            .containsOnly(clientId)
        assertThat(template.headers()["X-Naver-Client-Secret"])
            .containsOnly(clientSecret)
    }
}