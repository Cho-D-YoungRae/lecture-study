package org.example.concurrency.chapter03.exam05;

public class ThreadPriorityExample {

    public static void main(String[] args) throws InterruptedException {
        final CountingThread maxThread = new CountingThread();
        maxThread.setPriority(Thread.MAX_PRIORITY);
        maxThread.setName("maxThread");

        final CountingThread normThread = new CountingThread();
        normThread.setPriority(Thread.NORM_PRIORITY);
        normThread.setName("normThread");

        final CountingThread minThread = new CountingThread();
        minThread.setPriority(Thread.MIN_PRIORITY);
        minThread.setName("minThread");

        maxThread.start();
        normThread.start();
        minThread.start();

        maxThread.join();
        normThread.join();
        minThread.join();

        System.out.println("작업 완료...");
    }

    static class CountingThread extends Thread {

        private static final int MAX_COUNT = 1000000000;
        private int count;

        @Override
        public void run() {
            while (count < MAX_COUNT) {
                count++;
            }
            final Thread currentThread = Thread.currentThread();
            System.out.println(String.format(
                    "%s[%d]: 작업 완료", currentThread.getName(), currentThread.getPriority()
            ));
        }
    }
}
