package com.library.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.exception.ApiException
import com.library.exception.ErrorType
import com.library.NaverErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

class NaverErrorDecoder(
    private val objectMapper: ObjectMapper
): ErrorDecoder {

    private val log: Logger = LoggerFactory.getLogger(javaClass);

    override fun decode(methodKey: String?, response: Response?): Exception {
        try {
            val body = String(response!!.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8)
            val errorResponse = objectMapper.readValue(body, NaverErrorResponse::class.java)
//            throw RuntimeException(errorResponse.errorMessage)
            throw ApiException(
                errorResponse.errorMessage,
                ErrorType.EXTERNAL_API_ERROR,
                HttpStatus.valueOf(response.status())
            )
        } catch (e: IOException) {
            log.error("에러 메시지 파싱 에러 code={}, request={}, methodKey={}, errorMessage={}",
                response?.status(), response?.request(), methodKey, e.message)
            throw ApiException(
                "네이버 메시지 파싱 에러",
                ErrorType.EXTERNAL_API_ERROR,
                HttpStatus.valueOf(response!!.status())
            )
        }
    }
}