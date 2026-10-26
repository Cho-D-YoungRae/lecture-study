package com.example.parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.util.MyLogger.log;

public class ParallelMain6 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService requestPool = Executors.newFixedThreadPool(100);

        // logic 처리 전영 스레드 풀
        ExecutorService logicPool = Executors.newFixedThreadPool(400);
        int nThreads = 100;   // 1, 2, 3, 10, 20
        for (int i = 1; i <= nThreads; i++) {
            String requestName = "request" + i;
            requestPool.submit(() -> logic(requestName, logicPool));
            Thread.sleep(100);  // 스레드 순서를 확인하기 위해 약간 대기
        }
        requestPool.close();
        logicPool.close();
    }

    private static void logic(String requestName, ExecutorService es) {
        log("[" + requestName + "] START");
        long startTime = System.currentTimeMillis();

        // 1 부터 4까지의 작업을 각각 스레드풀에 제출
        Future<Integer> f1 = es.submit(() -> HeavyJob.heavyTask(1, requestName));
        Future<Integer> f2 = es.submit(() -> HeavyJob.heavyTask(2, requestName));
        Future<Integer> f3 = es.submit(() -> HeavyJob.heavyTask(3, requestName));
        Future<Integer> f4 = es.submit(() -> HeavyJob.heavyTask(4, requestName));

        int sum = 0;
        try {
            sum = f1.get() + f2.get() + f3.get() + f4.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        log("[" + requestName + "] time: " + time + "ms, sum=" + sum);
    }

}
