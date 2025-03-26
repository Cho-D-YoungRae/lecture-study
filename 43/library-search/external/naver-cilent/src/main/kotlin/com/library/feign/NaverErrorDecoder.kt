package com.library.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.NaverErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

class NaverErrorDecoder(
    private val objectMapper: ObjectMapper
): ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        try {
            val body = String(response!!.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8)
            val errorResponse = objectMapper.readValue(body, NaverErrorResponse::class.java)
            throw RuntimeException(errorResponse.errorMessage)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}