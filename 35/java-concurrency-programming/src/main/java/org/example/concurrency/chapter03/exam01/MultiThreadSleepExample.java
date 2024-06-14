package org.example.concurrency.chapter03.exam01;

public class MultiThreadSleepExample {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + ": 1초 후에 메시지가 출력됩니다");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ": Hello World");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + ": 2초 후에 메시지가 출력됩니다");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + ": Hello World");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(Thread.currentThread().getName() + ": 여기는 메인입니다.");
        thread1.start();
        thread2.start();
    }
}
