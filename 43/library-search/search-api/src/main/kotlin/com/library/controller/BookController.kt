package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.service.BookApplicationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/v1/books")
class BookController(
    private val bookApplicationService: BookApplicationService,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun search(
        @Validated @ModelAttribute request: SearchRequest
    ): PageResult<SearchResponse> {
        log.info("search query={}, page={}, size={}", request.query, request.page, request.size)
        return bookApplicationService.search(request.query, request.page, request.size)
    }

    @GetMapping("/stats")
    fun findQueryStats(
        @RequestParam query: String,
        @RequestParam date: LocalDate
    ): StatResponse {
        log.info("find stats query={}, date={}", query, date)
        return bookApplicationService.findQueryCount(query, date)
    }

    @GetMapping("/stats/ranking")
    fun findTop5Stats(): List<StatResponse> {
        log.info("find top 5 stats")
        return bookApplicationService.findTop5Query()
    }
}