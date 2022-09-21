package com.example.batchpractice.ch03;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

/**
 * 3-4
 */
@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    String name = jobParameters.getString("name");
                    Date date = jobParameters.getDate("date");
                    Long seq = jobParameters.getLong("seq");
                    Double age = jobParameters.getDouble("age");

                    System.out.println("=".repeat(30));
                    System.out.println("step1 was executed");
                    System.out.printf("name = %s, date = %s, seq = %d, age=%f%n", name, date, seq, age);

                    // JobParameters 를 가져오는 또 다른 방식
                    // 해당 시점의 값만 담겨오기 때문에 JobParameter 가 변경되도 반영되지 않을 수 있음
                    Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
                    System.out.println("jobParametersMap = " + jobParametersMap);
                    System.out.println("=".repeat(30));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
