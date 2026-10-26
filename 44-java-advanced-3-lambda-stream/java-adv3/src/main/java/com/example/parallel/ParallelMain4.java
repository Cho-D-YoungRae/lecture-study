package com.example.parallel;

import com.example.util.MyLogger;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static com.example.util.MyLogger.log;

public class ParallelMain4 {

    public static void main(String[] args) {
        int processorCount = Runtime.getRuntime().availableProcessors();
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        log("processorCount = " + processorCount + ", commonPool = " + commonPool.getParallelism());

        long startTime = System.currentTimeMillis();

        int sum = IntStream.rangeClosed(1, 8)
                .parallel()
                .map(HeavyJob::heavyTask)
                .reduce(0, Integer::sum);

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        MyLogger.log("time: " + time + "ms, sum: " + sum);
    }
}
