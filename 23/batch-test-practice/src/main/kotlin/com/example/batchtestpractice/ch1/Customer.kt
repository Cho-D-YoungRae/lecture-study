package com.example.batchtestpractice.ch1

import java.time.LocalDate

data class Customer(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
)
