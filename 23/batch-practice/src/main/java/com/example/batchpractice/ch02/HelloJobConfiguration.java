package com.example.batchpractice.ch02;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

/**
 * ~ 2-3
 */
//@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .next(helloStep2())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("=".repeat(30));
                    System.out.println(" >> Hello Spring Batch!!");
                    System.out.println("=".repeat(30));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("=".repeat(30));
                    System.out.println(" >> Step2 was executed");
                    System.out.println("=".repeat(30));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
