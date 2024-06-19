package org.example.concurrency.chapter03.exam04;

public class ThreadAliveExample {

    public static void main(String[] args) throws InterruptedException {
        // 두 개의 스레드 생성
        Thread task1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("task1 스레드 작업중...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("task1 스레드 종료...");
        });

        Thread task2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("task2 스레드 작업중...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("task2 스레드 종료...");
        });

        // 스레드 시작
        task1.start();
        task2.start();

        while (task1.isAlive() || task2.isAlive()) {
            System.out.println(String.format(
                    "스레드1 상태: %s, 스레드2 상태: %s",
                    task1.isAlive(), task2.isAlive()
            ));
            Thread.sleep(100);
        }

        System.out.println("모든 스레드 작업 종료");
    }
}
