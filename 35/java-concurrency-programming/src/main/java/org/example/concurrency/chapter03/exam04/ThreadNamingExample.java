package org.example.concurrency.chapter03.exam04;

public class ThreadNamingExample {

    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new Thread(() -> {
            System.out.println("스레드 이름: " + Thread.currentThread().getName());
        }, "myThread");
        myThread.start();
        myThread.join();

        Thread yourThread = new Thread(() -> {
            System.out.println("스레드 이름: " + Thread.currentThread().getName());
        });
        yourThread.setName("yourThread");
        yourThread.start();
        yourThread.join();

        for (int i = 0; i < 5; i++) {
            Thread defaultThread = new Thread(() -> {
                System.out.println("스레드 이름: " + Thread.currentThread().getName());
            });
            defaultThread.start();
            defaultThread.join();
        }
    }
}
