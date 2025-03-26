package com.library.config

import com.library.ErrorType

data class ErrorResponse(
    val errorMessage: String,
    val errorType: ErrorType
)
