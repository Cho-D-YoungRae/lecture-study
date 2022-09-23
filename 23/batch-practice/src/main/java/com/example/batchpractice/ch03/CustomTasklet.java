package com.example.batchpractice.ch03;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

// 빈으로 등록해서 사용해도 됨 -> 다른 빈을 Autowired 받아서 사용할 수 있음
public class CustomTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("custom step was executed");
        return RepeatStatus.FINISHED;
    }
}
