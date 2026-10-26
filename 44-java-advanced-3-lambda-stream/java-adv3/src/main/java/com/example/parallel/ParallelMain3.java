package com.example.parallel;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.util.MyLogger.log;

public class ParallelMain3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 스레드 풀을 준비한다.
        ExecutorService es = Executors.newFixedThreadPool(2);

        long startTime = System.currentTimeMillis();

        // 1. Fork 작업을 분할한다.
        SumTask task1 = new SumTask(1, 4);
        SumTask task2 = new SumTask(5, 8);

        // 2. 분할한 작업을 처리한다.
        Future<Integer> future1 = es.submit(task1);
        Future<Integer> future2 = es.submit(task2);

        // 3. join - 처리한 결과를 합친다.
        Integer result1 = future1.get();
        Integer result2 = future2.get();
        log("main 스레드 대기 완료");

        int sum = result1 + result2;

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        log("time: " + time + "ms, sum: " + sum);

        es.close();
    }

    static class SumTask implements Callable<Integer> {

        int startValue;
        int endValue;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public Integer call() {
            log("작업 시작");
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                int calculated = HeavyJob.heavyTask(i);
                sum += calculated;
            }

            log("작업 완료. result=" + sum);
            return sum;
        }
    }
}
