package com.library.feign

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.test.context.ActiveProfiles

@Disabled
@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
class NaverClientTest {

    @Autowired
    lateinit var naverClient: NaverClient

    @Test
    fun callNaver() {
        val http = naverClient.search("http", 1, 10)
        println(http)

        assertThat(http).isNotNull()
    }
}

@EnableAutoConfiguration
@EnableFeignClients(clients = [NaverClient::class])
class TestConfig {}