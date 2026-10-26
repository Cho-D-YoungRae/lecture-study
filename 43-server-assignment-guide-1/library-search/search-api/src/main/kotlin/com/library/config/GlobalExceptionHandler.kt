package com.library.config

import com.library.exception.ApiException
import com.library.exception.ErrorType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.resource.NoResourceFoundException

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

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(exception: NoResourceFoundException): ResponseEntity<ErrorResponse> {
        log.error("No Resource Found Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.badRequest()
            .body(ErrorResponse(ErrorType.NO_RESOURCE.description, ErrorType.NO_RESOURCE))
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun handleMissingServletRequestPartException(exception: MissingServletRequestPartException): ResponseEntity<ErrorResponse> {
        log.error("Missing Servlet Request Part Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.badRequest()
            .body(ErrorResponse(ErrorType.INVALID_PARAMETER.description, ErrorType.INVALID_PARAMETER))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        log.error("Method Argument Type Mismatch Exception occurred. message={}, className={}", exception.message, exception.javaClass.name)
        return ResponseEntity.badRequest()
            .body(ErrorResponse(ErrorType.INVALID_PARAMETER.description, ErrorType.INVALID_PARAMETER))
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