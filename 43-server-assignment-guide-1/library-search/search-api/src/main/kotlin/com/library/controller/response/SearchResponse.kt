package com.library.controller.response

import java.time.LocalDate

data class SearchResponse(
    val title: String,
    val author: String,
    val publisher: String,
    val pubDate: LocalDate,
    val isbn: String,
)
