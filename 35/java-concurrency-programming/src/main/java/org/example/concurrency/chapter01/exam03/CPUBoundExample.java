package org.example.concurrency.chapter01.exam03;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CPUBoundExample {

    public static void main(String[] args) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final Future<?> future = executorService.submit(() -> {

                // CPU 연산이 집중되고 오래 걸리는 작업
                long result = 0;
                for (int j = 0; j < 1000000000L; j++) {
                    result += j;
                }

                // 아주 잠깐 대기함
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Cpu Bound 일때 ContextSwitching 은 크게 발생하지 않는다
                System.out.println("스레드: " + Thread.currentThread().getName() + ", " + result);
            });
            futures.add(future);
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        long endTime = System.currentTimeMillis();
        System.out.println("CPU 개수를 초과하는 데이터를 병렬로 처리하는 데 걸린 시간: " + (endTime - startTime) + "ms");
        executorService.shutdown();
    }
}
