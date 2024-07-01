package org.example.concurrency.chapter05.exam04;

public class ThreadSafeLocalReferenceExample {

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
        // 지역 객체 참조. 각 스레드는 이 객체의 독립된 인스턴스를 가짐.
        LocalObject object = new LocalObject();

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
        final ThreadSafeLocalReferenceExample example = new ThreadSafeLocalReferenceExample();

        new Thread(() -> {
            example.useLocalObject();
        }, "스레드1").start();

        new Thread(() -> {
            example.useLocalObject();
        }, "스레드2").start();
    }
}
