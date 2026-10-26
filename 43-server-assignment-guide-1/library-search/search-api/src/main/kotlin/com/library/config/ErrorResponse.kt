package com.library.config

import com.library.exception.ErrorType

data class ErrorResponse(
    val errorMessage: String,
    val errorType: ErrorType
)
