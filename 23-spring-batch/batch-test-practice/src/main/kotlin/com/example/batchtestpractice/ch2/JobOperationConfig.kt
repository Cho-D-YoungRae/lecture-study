package com.example.batchtestpractice.ch2

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JobOperationConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val jobRegistry: JobRegistry
) {

    fun jobOpJob(
        jobOpStep1: Step,
        jobOpStep2: Step
    ) = jobBuilderFactory.get("jobOpJob")
        .incrementer(RunIdIncrementer())
        .start(jobOpStep1)
        .next(jobOpStep2)
        .build()

    @Bean
    fun jobOpStep1() = stepBuilderFactory.get("jobOpStep1")
        .tasklet { contribution, chunkContext ->
            println("step1 was executed")
            Thread.sleep(5000)
            RepeatStatus.FINISHED
        }
        .build()

    @Bean
    fun jobOpStep2() = stepBuilderFactory.get("jobOpStep2")
        .tasklet { contribution, chunkContext ->
            println("step2 was executed")
            Thread.sleep(5000)
            RepeatStatus.FINISHED
        }
        .build()

    @Bean
    fun jobRegistryBeanPostProcessor(): JobRegistryBeanPostProcessor {
        val postProcessor = JobRegistryBeanPostProcessor()
        postProcessor.setJobRegistry(jobRegistry)
        return postProcessor
    }
}