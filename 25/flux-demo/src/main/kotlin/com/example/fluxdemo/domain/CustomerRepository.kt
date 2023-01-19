package com.example.fluxdemo.domain

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface CustomerRepository : R2dbcRepository<Customer, Long> {

    @Query("""
        select * from customer where last_name = :lastName
    """)
    fun findAllByLastName(lastName: String) : Flux<Customer>
}