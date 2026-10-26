package org.example.concurrency.chapter04.exam05;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolThreadLocalExample {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // 첫 번째 작업: threadLocal에 값 설정
        executor.submit(() -> {
            threadLocal.set("첫 번째 작업");
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            // 작업이 끝나면 threadLocal 값을 제거
            // -> 이렇게 하지 않으면 다음 작업에서 threadLocal 값이 남아 있을 수 있음
//            threadLocal.remove();
        });

        // 잠시 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-".repeat(50));

        // 여러 번의 두 번째 작업: threadLocal 값을 설정하지 않고 바로 값을 가져와 출력
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            });
        }

        executor.shutdown();
    }
}
