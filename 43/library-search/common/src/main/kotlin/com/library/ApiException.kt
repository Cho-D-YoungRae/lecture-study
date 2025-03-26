package com.library

import org.springframework.http.HttpStatus

class ApiException: RuntimeException {

    val errorMessage: String
    val errorType: ErrorType
    val httpStatus: HttpStatus

    constructor(errorMessage: String, errorType: ErrorType, httpStatus: HttpStatus) : super(errorMessage) {
        this.errorMessage = errorMessage
        this.errorType = errorType
        this.httpStatus = httpStatus
    }
}