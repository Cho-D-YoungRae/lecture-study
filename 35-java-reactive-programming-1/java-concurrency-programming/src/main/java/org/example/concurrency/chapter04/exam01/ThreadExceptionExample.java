package org.example.concurrency.chapter04.exam01;

public class ThreadExceptionExample {

    public static void main(String[] args) {
        try {
            final Thread thread = new Thread(() -> {
                throw new RuntimeException("스레드 예외 발생");
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("예외 처리" + e);
        }
    }
}
