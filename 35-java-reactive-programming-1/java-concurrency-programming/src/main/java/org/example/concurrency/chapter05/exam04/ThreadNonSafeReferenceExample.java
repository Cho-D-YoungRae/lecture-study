package org.example.concurrency.chapter05.exam04;

public class ThreadNonSafeReferenceExample {

    LocalObject object = new LocalObject();

    class LocalObject{
        private int value;

        public void increment() {
            value++;
        }

        @Override
        public String toString() {
            return "LocalObject{" +
                    "value=" + value +
                    '}';
        }
    }

    public void useLocalObject() {
        for (int i = 0; i < 5; i++) {
            object.increment();
            System.out.println(Thread.currentThread().getName() + " : " + object);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ThreadNonSafeReferenceExample example = new ThreadNonSafeReferenceExample();

        new Thread(() -> {
            example.useLocalObject();
        }, "스레드1").start();

        new Thread(() -> {
            example.useLocalObject();
        }, "스레드2").start();
    }
}
