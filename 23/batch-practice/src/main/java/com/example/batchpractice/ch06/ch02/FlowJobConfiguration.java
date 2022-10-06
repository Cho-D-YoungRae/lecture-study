package com.example.batchpractice.ch06.ch02;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(flowA())
                .next(stepA())
                .next(flowB())
                .next(stepB())
                .end()
                .build();
    }

    @Bean
    public Flow flowA() {
        return new FlowBuilder<Flow>("flowA")
                .start(step1())
                .next(step2())
                .end();
    }

    @Bean
    public Flow flowB() {
        return new FlowBuilder<Flow>("flowB")
                .start(step3())
                .next(step4())
                .end();
    }

    @Bean
    public Step stepA() {
        return stepBuilderFactory.get("stepA")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> stepA was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> stepB was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step1 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step2 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step3 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step4 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
