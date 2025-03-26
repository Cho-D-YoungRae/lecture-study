package com.library

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverBookItemResponse(
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val discount: String,
    val publisher: String,
    val isbn: String,
    val description: String,
    @JsonProperty("pubdate") val pubDate: String,
)
