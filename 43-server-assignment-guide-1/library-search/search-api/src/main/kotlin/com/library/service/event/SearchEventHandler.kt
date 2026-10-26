package com.library.service.event

import com.library.entity.DailyStat
import com.library.service.DailyStatCommandService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SearchEventHandler(
    private val dailyStatCommandService: DailyStatCommandService,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun handlerEvent(event: SearchEvent) {
        Thread.sleep(5000)  // 의도한대로 이벤트가 비동기적으로 처리되는지 확인하기 위해 지연
        log.info("handleEvent: {}", event)
        val dailyStat = DailyStat(query = event.query, eventDateTime = event.timestamp)
        dailyStatCommandService.save(dailyStat)
    }
}