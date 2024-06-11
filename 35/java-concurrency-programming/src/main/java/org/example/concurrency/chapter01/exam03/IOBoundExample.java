package org.example.concurrency.chapter01.exam03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 이 예재로 CPU 바운드, IO 바운드의 정확한 평가를 할 수는 없으나,
 * 우리가 배운 이론의 컨셉, 개념을 이해하는데 어느정도 도움이 될 것임
 */
public class IOBoundExample {

    public static void main(String[] args) {
        int numThreads = Runtime.getRuntime().availableProcessors() / 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {

                    // IO 가 집중 되는 작업
                    for (int j = 0; j < 5; j++) {
                        Files.readAllLines(Path.of("/Users/choyoungrae/Desktop/lecture-study/35/java-concurrency-programming/src/main/java/org/example/concurrency/chapter01/exam03/sample.txt"));
                        System.out.println("스레드: " + Thread.currentThread().getName() +", " +j); // IO Bound 일때 ContextSwitching 이 일어난다
                    }

                    // 아주 빠른 Cpu 연산
                    int result = 0;
                    for (long j = 0; j < 10; j++) {
                        result += j;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }
}
