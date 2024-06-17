package org.example.concurrency.chapter03.exam03;

public class InterruptedExample3 {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
                if (Thread.interrupted()) {
                    System.out.println(Thread.currentThread().getName() + " 인터럽트 상태가 초기화 되어습니다.");
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });

        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread.interrupt();
    }
}
