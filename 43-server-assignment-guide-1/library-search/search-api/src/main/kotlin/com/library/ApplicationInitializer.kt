package com.library

import com.library.entity.DailyStat
import com.library.repository.DailyStatRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ApplicationInitializer(
    private val dailyStatRepository: DailyStatRepository
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        dailyStatRepository.saveAll(listOf(
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),
            DailyStat("HTTP", LocalDateTime.now()),

            DailyStat("JAVA", LocalDateTime.now()),
            DailyStat("JAVA", LocalDateTime.now()),
            DailyStat("JAVA", LocalDateTime.now()),

            DailyStat("Kotlin", LocalDateTime.now()),

            DailyStat("Database", LocalDateTime.now()),

            DailyStat("OS", LocalDateTime.now()),
        ))
    }
}