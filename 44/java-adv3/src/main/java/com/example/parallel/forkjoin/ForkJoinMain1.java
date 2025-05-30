package com.example.parallel.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static com.example.util.MyLogger.log;

public class ForkJoinMain1 {

    public static void main(String[] args) {
        List<Integer> data = IntStream.rangeClosed(1, 8)
                .boxed()
                .toList();

        log("[셍성]" + data);

        // ForkJoinPool 생성 및 작업 수행
        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.currentTimeMillis();

        SumTask task = new SumTask(data);
        Integer result = pool.invoke(task);

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        log("time: " + time + "ms, sum: " + result);
        log("pool: " + pool);
        pool.close();
    }
}
