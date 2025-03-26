package com.library.service

import com.library.controller.response.StatResponse
import com.library.repository.DailyStatRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class DailyStatsQueryService(
    private val dailyStatRepository: DailyStatRepository,
) {

    fun findQueryCount(query: String, date: LocalDate): StatResponse {
        return StatResponse(
            query,
            dailyStatRepository.countByQueryAndEventDateTimeBetween(
                query, date.atStartOfDay(), date.atTime(LocalTime.MAX))
        )
    }


}