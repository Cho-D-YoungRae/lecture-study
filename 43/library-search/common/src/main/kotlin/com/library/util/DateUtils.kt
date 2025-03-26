package com.library.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")

fun parse(date: String): LocalDate {
    return LocalDate.parse(date, YYYYMMDD_FORMATTER)
}