package com.library.controller.request

import jakarta.validation.constraints.*

data class SearchRequest(
    @field:NotBlank(message = "입력은 비어있을 수 없습니다.")
    @field:Size(max = 50, message = "query는 50자를 초과할 수 없습니다.")
    val query: String,
    @field:NotNull(message = "페이지 번호는 필수입니다.")
    @field:Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    @field:Max(value = 10000, message = "페이지 번호는 10000 이하이어야 합니다.")
    val page: Int,
    @field:NotNull(message = "사이즈는 필수입니다.")
    @field:Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @field:Max(value = 50, message = "사이즈는 50 이하이어야 합니다.")
    val size: Int,
)
