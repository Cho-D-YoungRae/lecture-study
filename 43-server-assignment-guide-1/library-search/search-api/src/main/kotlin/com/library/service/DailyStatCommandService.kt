package com.library.service

import com.library.entity.DailyStat
import com.library.repository.DailyStatRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DailyStatCommandService(
    private val dailyStatRepository: DailyStatRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun save(dailyStat: DailyStat) {
        log.info("save daily stat: {}", dailyStat)
        dailyStatRepository.save(dailyStat)
    }
}