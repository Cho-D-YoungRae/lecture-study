package com.example.batchpractice.ch03;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 3-4
 * spring.batch.job.enabled = false => Spring boot Job 자동 실행을 끄고 사용
 */
@Component
@RequiredArgsConstructor
public class JobParameterTest implements ApplicationRunner {

    // 스프링 배치가 초기화 될 때 빈으로 등록되어 있음
    private final JobLauncher jobLauncher;

    private final Job job;

    // spring.batch.job.enabled = true 일 때 아래의 명령과 같이 가능
    // java -jar batch-practice-0.0.1-SNAPSHOP.jar name=user1 seq(long)=2L date(date)=2021/01/01 age(double)=16.5
    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("seq", 2L)
                .addDate("date", new Date())
                .addDouble("age", 16.5)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
