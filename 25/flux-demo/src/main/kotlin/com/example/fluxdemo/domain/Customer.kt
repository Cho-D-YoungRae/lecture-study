package com.example.fluxdemo.domain

import org.springframework.data.annotation.Id

class Customer(
    val firstName: String,
    val lastName: String
) {
    @Id var id: Long? = null

    override fun toString(): String {
        return "Customer(firstName='$firstName', lastName='$lastName', id=$id)"
    }

}