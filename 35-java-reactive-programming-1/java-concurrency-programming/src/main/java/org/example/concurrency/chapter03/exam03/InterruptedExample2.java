package org.example.concurrency.chapter03.exam03;

public class InterruptedExample2 {

    public static void main(String[] args) {
        Thread thread2 = new Thread(() -> {
            while (!Thread.interrupted()) {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
            }
            System.out.println(Thread.currentThread().getName() + ": 스레드 인터럽트...");
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
                if (i == 2) {
                    System.out.println(Thread.currentThread().getName() + " 가 " + thread2.getName() + " 을 인터럽트 합니다.");
                    thread2.interrupt();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        });

        thread2.start();
        thread1.start();
    }
}
