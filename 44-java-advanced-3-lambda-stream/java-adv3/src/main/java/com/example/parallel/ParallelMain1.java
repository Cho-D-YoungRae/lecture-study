package com.example.parallel;

import com.example.util.MyLogger;

import java.util.stream.IntStream;

public class ParallelMain1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        int sum = IntStream.rangeClosed(1, 8)
                .map(HeavyJob::heavyTask)
                .reduce(0, Integer::sum);

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        MyLogger.log("time: " + time + "ms, sum: " + sum);
    }
}
