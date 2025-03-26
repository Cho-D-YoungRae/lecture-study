package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.BookApplicationService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/books")
class BookController(
    private val bookApplicationService: BookApplicationService,
) {

    @GetMapping
    fun search(
        @Validated @ModelAttribute request: SearchRequest
    ): PageResult<SearchResponse> {
        return bookApplicationService.search(request.query, request.page, request.size)
    }
}