package org.example.concurrency.chapter07.exam03;

public class VolatileExample {

    private volatile boolean running = true;

    public void volatileTest() {
        new Thread(() -> {
            int count = 0;
            while (running) {
                count++;
            }

            System.out.println(Thread.currentThread().getName() + ": 종료. count=" + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": 종료중...");
            running = false;
        }).start();
    }

    public static void main(String[] args) {

        VolatileExample volatileExample = new VolatileExample();
        volatileExample.volatileTest();
    }
}
