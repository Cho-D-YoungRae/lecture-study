package org.example.concurrency.chapter04.exam02;

/**
 * running 에 volatile 키워드가 없어서 정상적으로 종료가 안됨
 */
public class FlagThreadStopExample1 {

    private static boolean running = true;

    public static void main(String[] args) {

        new Thread(() -> {
            int count = 0;
            while (running) {
                count++;
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            System.out.println("count: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            running = false;
        }).start();

    }
}
