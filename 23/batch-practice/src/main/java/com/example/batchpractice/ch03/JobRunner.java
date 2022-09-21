package com.example.batchpractice.ch03;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 3-3
 * spring.batch.job.enabled = false 로 Job 자동 실행을 끄고 사용
 */
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    // 스프링 배치가 초기화 될 때 빈으로 등록되어 있음
    private final JobLauncher jobLauncher;

    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user2")
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
