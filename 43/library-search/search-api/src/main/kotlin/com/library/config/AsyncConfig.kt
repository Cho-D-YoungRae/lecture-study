package com.library.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor

@Configuration
class AsyncConfig: AsyncConfigurer {

    @Bean("lsExecutor")
    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        Runtime.getRuntime().availableProcessors().let {
            executor.corePoolSize = it
            executor.maxPoolSize = it * 2
        }
        executor.queueCapacity = 100
        executor.keepAliveSeconds = 60
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setAwaitTerminationSeconds(60)
        executor.setThreadNamePrefix("LS-")
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return super.getAsyncUncaughtExceptionHandler()
    }
}

class CustomAsyncExceptionHandler: AsyncUncaughtExceptionHandler {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(
        throwable: Throwable,
        method: Method,
        vararg obj: Any
    ) {
        log.error("Failed to execute {}", throwable.message)
    }
}