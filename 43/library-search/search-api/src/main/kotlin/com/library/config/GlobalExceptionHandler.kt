package com.library.config

import com.library.ApiException
import com.library.ErrorType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log: Logger = LoggerFactory.getLogger(javaClass);

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<ErrorResponse> {
        log.error("Api Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.status(exception.httpStatus)
            .body(ErrorResponse(exception.errorMessage, exception.errorType))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        log.error("Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.internalServerError()
            .body(ErrorResponse(ErrorType.UNKNOWN.description, ErrorType.UNKNOWN))
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(exception: BindException): ResponseEntity<ErrorResponse> {
        log.error("Bind Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.badRequest()
            .body(ErrorResponse(createMessage(exception), ErrorType.INVALID_PARAMETER))
    }

    private fun createMessage(exception: BindException): String {
        return exception.bindingResult.fieldErrors.joinToString(", ") { "${it.field} : ${it.defaultMessage}" }
    }
}