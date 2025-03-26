package com.library.repository

import com.library.entity.DailyStat
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface DailyStatRepository: JpaRepository<DailyStat, Long> {

    fun countByQueryAndEventDateTimeBetween(
        query: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Long
}