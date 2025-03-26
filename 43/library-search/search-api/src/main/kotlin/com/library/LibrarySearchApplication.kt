package com.library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibrarySearchApplication

fun main(args: Array<String>) {
    runApplication<LibrarySearchApplication>(*args)
}