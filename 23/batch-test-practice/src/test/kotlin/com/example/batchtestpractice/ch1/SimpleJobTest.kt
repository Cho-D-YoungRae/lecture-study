package com.example.batchtestpractice.ch1

import com.example.batchtestpractice.TestBatchConfig
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.batch.core.*
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.util.Date

@SpringBatchTest
@SpringBootTest(classes = [SimpleJobConfig::class, TestBatchConfig::class])
internal class SimpleJobTest {

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @AfterEach
    fun afterEach() {
        jdbcTemplate.execute("delete from customer")
    }

    @Test
    fun simpleJob_Test() {
        // given
        val jobParameters = JobParametersBuilder()
            .addString("name", "user1")
            .addLong("date", Date().time)
            .toJobParameters()

        // when
        val jobExecution: JobExecution = jobLauncherTestUtils.launchJob(jobParameters)
        val jobExecution1 = jobLauncherTestUtils.launchStep("step1")

        // then
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(jobExecution.exitStatus).isEqualTo(ExitStatus.COMPLETED)

        val stepExecution = (jobExecution1.stepExecutions as List<StepExecution>)[0]
        assertThat(stepExecution.commitCount).isOne
        assertThat(stepExecution.readCount).isZero
        assertThat(stepExecution.writeCount).isZero
    }
}