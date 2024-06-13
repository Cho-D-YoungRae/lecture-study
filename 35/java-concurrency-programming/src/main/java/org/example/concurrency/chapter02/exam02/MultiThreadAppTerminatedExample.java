package org.example.concurrency.chapter02.exam02;

public class MultiThreadAppTerminatedExample {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new MyRunnable(i));
            thread.start();
        }

        System.out.println("메인 스레드 종료...");
    }

    private static class MyRunnable implements Runnable {

        private int id;

        private MyRunnable(final int id) {
            this.id = id;
        }


        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
            firstMethod(id);
        }

        private void firstMethod(int id) {
            int localValue = id + 100;
            secondMethod(localValue);
        }

        private void secondMethod(int localValue) {
            System.out.println(Thread.currentThread().getName() + " : 스레드 ID=" + id + ", Value=" + localValue);
        }
    }
}
