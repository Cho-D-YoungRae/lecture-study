package org.example.concurrency.chapter05.exam03;

public class CriticalSectionExample {

    public static void main(String[] args) {
        final SharedResource resource = new SharedResource();

        Thread thread1 = new Thread(() -> {
            resource.increment();
        });

        Thread thread2 = new Thread(() -> {
            resource.increment();
        });

        thread1.start();
        thread2.start();
    }

    static class SharedResource {
        private int count = 0;

        public void increment() {
            for (int i = 0; i < 100000; i++) {
                synchronized (this) { // Entry Section
                    // Critical Section
                    count++;
                    System.out.println(Thread.currentThread().getName() + " : " + count);
                } // Exit Section}
            }

            // Remainder Section
            doOtherWork();
        }
        private void doOtherWork() {
            System.out.println(Thread.currentThread().getName() + " : critical section 외부 작업");
        }
    }
}
