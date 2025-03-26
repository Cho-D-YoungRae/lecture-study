package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.service.event.SearchEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BookApplicationService(
    private val bookQueryService: BookQueryService,
    private val dailyStatsQueryService: DailyStatsQueryService,
    private val eventPublisher: ApplicationEventPublisher
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        val response = bookQueryService.search(query, page, size)
        if (response.contents.isNotEmpty()) {
            log.info("검색 결과 개수: ${response.contents.size}")
            eventPublisher.publishEvent(SearchEvent(query, LocalDateTime.now()))
        }
        return response
    }

    fun findQueryCount(query: String, date: LocalDate): StatResponse {
        return dailyStatsQueryService.findQueryCount(query, date)
    }

    fun findTop5Query(): List<StatResponse> {
        return dailyStatsQueryService.findTop5Query()
    }
}