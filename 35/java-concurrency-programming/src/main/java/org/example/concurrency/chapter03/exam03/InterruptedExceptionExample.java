package org.example.concurrency.chapter03.exam03;

public class InterruptedExceptionExample {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태 1: " + Thread.currentThread().isInterrupted());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + ": 인터럽트 발생... 실행시간" + (endTime - startTime) + "ms");
                System.out.println(Thread.currentThread().getName() + " 인터럽트 상태 2: " + Thread.currentThread().isInterrupted());

                Thread.currentThread().interrupt();
            }

        });
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread.interrupt();
        thread.join();

        System.out.println(thread.getName() + " 인터럽트 상태3: " + thread.isInterrupted());
    }
}
