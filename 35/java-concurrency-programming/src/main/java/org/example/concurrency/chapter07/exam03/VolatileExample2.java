package org.example.concurrency.chapter07.exam03;

public class VolatileExample2 {

    private volatile int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        VolatileExample2 example = new VolatileExample2();

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
            System.out.println(Thread.currentThread().getName() + ": 쓰기 작업 완료");
        }, "쓰기 쓰레드");

        Runnable reader = () -> {
            int localvalue = -1;
            while (localvalue < 1000) {
                localvalue = example.getCount();
                System.out.println(Thread.currentThread().getName() + ": 읽은 값. count=" + localvalue);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        writer.start();
        for (int i = 0; i < 5; i++) {
            new Thread(reader, "읽기 쓰레드-" + i).start();
        }

    }
}
