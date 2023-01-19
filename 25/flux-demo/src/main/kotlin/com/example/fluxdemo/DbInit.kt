package com.example.fluxdemo

import com.example.fluxdemo.domain.Customer
import com.example.fluxdemo.domain.CustomerRepository
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

@Configuration
class DbInit {

    @Bean
    fun demo(repository: CustomerRepository): CommandLineRunner? {
        return CommandLineRunner { args ->
            // save a few customers
            repository.saveAll(
                listOf(
                    Customer("Jack", "Bauer"),
                    Customer("Chloe", "O'Brian"),
                    Customer("Kim", "Bauer"),
                    Customer("David", "Palmer"),
                    Customer("Michelle", "Dessler")
                )
            )
                .blockLast(Duration.ofSeconds(10))

            // fetch all customers
            log.info("Customers found with findAll():")
            log.info("-------------------------------")
            repository.findAll().doOnNext { customer -> log.info(customer.toString()) }
                .blockLast(Duration.ofSeconds(10))
            log.info("")

            // fetch an individual customer by ID
            repository.findById(1L).doOnNext { customer ->
                log.info("Customer found with findById(1L):")
                log.info("--------------------------------")
                log.info(customer.toString())
                log.info("")
            }.block(Duration.ofSeconds(10))


            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):")
            log.info("--------------------------------------------")
            repository.findAllByLastName("Bauer").doOnNext { bauer -> log.info(bauer.toString()) }
                .blockLast(Duration.ofSeconds(10))
            log.info("")
        }
    }

}