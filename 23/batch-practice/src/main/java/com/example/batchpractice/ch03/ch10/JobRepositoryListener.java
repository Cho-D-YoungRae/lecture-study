package com.example.batchpractice.ch03.ch10;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;

import static java.util.Objects.nonNull;

/**
 * 3-10
 */
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {

    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = jobExecution.getJobParameters();
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("requestDate", "20210102")
//                .toJobParameters();
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if (nonNull(lastJobExecution)) {
            for (StepExecution stepExecution : lastJobExecution.getStepExecutions()) {
                BatchStatus status = stepExecution.getStatus();
                System.out.println("status = " + status);
                ExitStatus exitStatus = stepExecution.getExitStatus();
                System.out.println("exitStatus = " + exitStatus);
                String stepName = stepExecution.getStepName();
                System.out.println("stepName = " + stepName);
            }
        }
    }
}
