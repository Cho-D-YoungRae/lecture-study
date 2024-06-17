package org.example.concurrency.chapter03.exam03;

public class InterruptExample {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 가 " + thread1.getName() + " 을 인터럽트 합니다.");
            thread1.interrupt();
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });

        thread2.start();
        Thread.sleep(5000);
        thread1.start();

        thread1.join();
        thread2.join();

        System.out.println("모든 스레드가 종료되었습니다.");
    }
}
