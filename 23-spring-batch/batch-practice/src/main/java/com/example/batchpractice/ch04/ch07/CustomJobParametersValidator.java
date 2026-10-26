package com.example.batchpractice.ch04.ch07;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import static java.util.Objects.isNull;

public class CustomJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (isNull(parameters.getString("name"))) {
            throw new JobParametersInvalidException("name parameters is not found");
        }
    }
}
