package com.example.batchpractice.ch03.ch11;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 3-11
 */
//@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final Job job;

    private final JobLauncher jobLauncher;

    private final BasicBatchConfigurer basicBatchConfigurer;

    @PostMapping("/batch")
    public String launch(@RequestParam String memberId)
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("memberId", memberId)
                .addDate("date", new Date())
                .toJobParameters();

        /*
         *  SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher) this.jobLauncher;
         *  이렇게 사용할 수 있는 이유는 this.jobLauncher 는 SimpleJobLauncher 를 상속하는 프록시 객체
         *  아래와 같이 사용하면 실제 SimpleJobLauncher 를 얻을 수 있음
         */
        SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        this.jobLauncher.run(job, jobParameters);

        return "Batch completed";
    }
}
