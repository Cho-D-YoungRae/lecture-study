package com.example.batchpractice.ch15.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FileSchJob extends QuartzJobBean {

    private final Job fileJob;

    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

        int jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);

        // 한번 실행했다면 다시 실행하지 않도록
        if (!jobInstances.isEmpty()) {
            for (JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream()
                        .filter(jobExecution -> Objects.equals(
                                jobExecution.getJobParameters().getString("requestDate"), requestDate))
                        .collect(Collectors.toList());
                if (!jobExecutionList.isEmpty()) {
                    throw new org.springframework.batch.core.JobExecutionException(requestDate + "already exists");
                }
            }
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                .toJobParameters();

        jobLauncher.run(fileJob, jobParameters);
    }
}
