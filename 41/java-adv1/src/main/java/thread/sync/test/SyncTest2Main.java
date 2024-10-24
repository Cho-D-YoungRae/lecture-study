package thread.sync.test;

import static util.MyLogger.log;

public class SyncTest2Main {

    public static void main(String[] args) {
        MyCounter myCounter = new MyCounter();

        Runnable task = () -> {
            myCounter.counter();
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();
    }

    static class MyCounter {

        public void counter() {
            int localValue = 0;
            for (int i = 0; i < 1000; i++) {
                localValue++;
            }
            log("결과: " + localValue);
        }
    }
}
