package com.example.batchpractice.ch15.batch.job.api;

import com.example.batchpractice.ch15.batch.listener.JobListener;
import com.example.batchpractice.ch15.batch.tasklet.ApiEndTasklet;
import com.example.batchpractice.ch15.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final ApiStartTasklet apiStartTasklet;

    private final ApiEndTasklet apiEndTasklet;

    private final Step jobStep;

    @Bean
    public Job apiJob() {
        return jobBuilderFactory.get("apiJob")
                .listener(new JobListener())
                .start(apiStep1())
                .next(jobStep)
                .next(apiStep2())
                .build();
    }

    private Step apiStep1() {
        return stepBuilderFactory.get("apiStep1")
                .tasklet(apiStartTasklet)
                .build();
    }

    private Step apiStep2() {
        return stepBuilderFactory.get("apiStep2")
                .tasklet(apiEndTasklet)
                .build();
    }

}
