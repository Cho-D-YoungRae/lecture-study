package com.example.fluxdemo.web

import com.example.fluxdemo.domain.Customer
import com.example.fluxdemo.domain.CustomerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest(CustomerController::class)
class CustomerControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @MockBean
    lateinit var customerRepository: CustomerRepository

    @Test
    fun `한건찾기 테스트`() {
        // given
        given(customerRepository.findById(1L))
            .willReturn(Mono.just(Customer("first", "last")))

        // when & then
        client.get()
            .uri("/customers/{id}", 1L)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.firstName").isEqualTo("first")
            .jsonPath("$.lastName").isEqualTo("last")
    }
}