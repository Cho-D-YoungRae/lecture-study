package org.example.concurrency.chapter02.exam03;

public class RunnableStateThreadExample {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("스레드 상태: " + Thread.currentThread().getState());
        });
        thread.start();
    }
}
