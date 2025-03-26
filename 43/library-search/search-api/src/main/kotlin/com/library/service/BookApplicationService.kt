package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.entity.DailyStat
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BookApplicationService(
    private val bookQueryService: BookQueryService,
    private val dailyStatCommandService: DailyStatCommandService,
    private val dailyStatsQueryService: DailyStatsQueryService,
) {

    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        val response = bookQueryService.search(query, page, size)
        val dailyStat = DailyStat(query = query, LocalDateTime.now())
        dailyStatCommandService.save(dailyStat)
        return response
    }

    fun findQueryCount(query: String, date: LocalDate): StatResponse {
        return dailyStatsQueryService.findQueryCount(query, date)
    }
}