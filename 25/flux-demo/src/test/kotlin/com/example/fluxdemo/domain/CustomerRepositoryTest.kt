package com.example.fluxdemo.domain

import com.example.fluxdemo.DbInit
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import reactor.test.StepVerifier

@DataR2dbcTest
@Import(DbInit::class)
class CustomerRepositoryTest {

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun `한건찾기 테스트`() {
        StepVerifier
            .create(customerRepository.findById(1L))
            .expectNextMatches { customer -> customer.firstName == "Jack" }
            .expectComplete()
            .verify()
    }
}