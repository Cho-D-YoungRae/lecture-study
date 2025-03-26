package com.library.exception

enum class ErrorType(
    val description: String
) {

    EXTERNAL_API_ERROR("외부 API 호출 에러입니다."),
    UNKNOWN("알 수 없는 에러입니다."),
    INVALID_PARAMETER("잘못된 요청값입니다."),
    NO_RESOURCE("존재하지 않는 리소스입니다."),

}