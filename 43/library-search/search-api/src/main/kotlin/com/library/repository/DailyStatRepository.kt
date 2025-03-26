package com.library.repository

import com.library.controller.response.StatResponse
import com.library.entity.DailyStat
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface DailyStatRepository: JpaRepository<DailyStat, Long> {

    fun countByQueryAndEventDateTimeBetween(
        query: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Long

    @Query("""
        select new com.library.controller.response.StatResponse(ds.query, count(ds.query))
        from DailyStat ds
        group by ds.query 
        order by count(ds.query) desc
    """)
    fun findTopQuery(pageable: Pageable): List<StatResponse>
}