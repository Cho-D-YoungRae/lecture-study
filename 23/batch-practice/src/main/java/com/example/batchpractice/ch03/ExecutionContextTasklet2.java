package com.example.batchpractice.ch03;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import static java.util.Objects.isNull;

/**
 * 3-9
 */
public class ExecutionContextTasklet2 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ExecutionContextTasklet2 Step was executed");

        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution().getJobExecution().getExecutionContext();
        ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();
        if (isNull(stepExecutionContext.get("stepName"))) {
            System.out.println("put stepName to StepExecutionContext");
            stepExecutionContext.put("stepName", stepName);
        }
        System.out.println("jobExecutionContext.get(\"jobName\") = " + jobExecutionContext.get("jobName"));
        System.out.println("stepExecutionContext.get(\"stepName\") = " + stepExecutionContext.get("stepName"));

        return RepeatStatus.FINISHED;
    }

}
