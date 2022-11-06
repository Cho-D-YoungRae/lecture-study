package com.example.batchpractice.ch15.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiStartTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 보통은 리스너를 이용. tasklet 을 이용해보기 위함
        System.out.println(" >> ApiService is started");

        return RepeatStatus.FINISHED;
    }

}
