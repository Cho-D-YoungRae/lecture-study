package org.example.concurrency.chapter03.exam02;

public class MultiThreadJoinExample {

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName() + ": 스레드 종료...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName() + ": 스레드 종료...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        System.out.println(Thread.currentThread().getName() + ": 다른 스레드의 완료를 기다립니다.");
        thread1.join();
        thread2.join();
        System.out.println(Thread.currentThread().getName() + ": 계속 작업을 진행합니다.");
    }
}
