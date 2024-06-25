package org.example.concurrency.chapter05.exam01;

public class MultiThreadExample {

    private static int sum = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 500 + 1; i++) {
                // 동기화 처리를 하지 않을 경우 결과가 달라질 수 있음
                synchronized (lock) {
                    sum += i;
                }
                try {
                    Thread.sleep(1);
                    // 멀티 스레드 어플리케이션이므로 예외 발생 시 프로그램이 종료되지 않음
                    // 사이드 이펙트는 발생할 수 있음
//                    throw new RuntimeException("예외 발생");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 501; i < 1000 + 1; i++) {
                synchronized (lock) {
                    sum += i;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("합계: " + sum);
        System.out.println("소요 시간: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
