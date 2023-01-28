package com.example.batchtestpractice

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
@EnableBatchProcessing
@EnableAutoConfiguration
class TestBatchConfig {
}