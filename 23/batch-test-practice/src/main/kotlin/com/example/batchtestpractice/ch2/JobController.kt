package com.example.batchtestpractice.ch2

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobInstance
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JobController(
    private val jobRegistry: JobRegistry,
    private val jobOperator: JobOperator,
    private val jobExplorer: JobExplorer
) {

    @PostMapping("/batch/start")
    fun start(@RequestBody jobInfo: JobInfo): String {
        // 한번 이상 실행이 되어야 DB에서 아래 값을 가져올 수 있음
        for (jobName in jobRegistry.jobNames) {
            val job: Job = jobRegistry.getJob(jobName)
            println("job name: ${job.name}")
            jobOperator.start(job.name, "id=" + jobInfo.id)
        }
        return "batch is started"
    }

    @PostMapping("/batch/restart")
    fun restart(): String {
        for (jobName in jobRegistry.jobNames) {
            val job: Job = jobRegistry.getJob(jobName)
            println("job name: ${job.name}")

            val lastJobInstance: JobInstance = jobExplorer.getLastJobInstance(jobName) ?: continue
            val lastJobExecution: JobExecution = jobExplorer.getLastJobExecution(lastJobInstance) ?: continue
            jobOperator.restart(lastJobExecution.id)
        }
        return "batch is restarted"
    }

    @PostMapping("/batch/stop")
    fun stop(): String {
        for (jobName in jobRegistry.jobNames) {
            val job: Job = jobRegistry.getJob(jobName)
            println("job name: ${job.name}")

            val runningJobExecutions: Set<JobExecution> = jobExplorer.findRunningJobExecutions(job.name)
            for (jobExecution in runningJobExecutions) {
                jobOperator.stop(jobExecution.id)
            }
        }

        return "batch is stopped"
    }
}