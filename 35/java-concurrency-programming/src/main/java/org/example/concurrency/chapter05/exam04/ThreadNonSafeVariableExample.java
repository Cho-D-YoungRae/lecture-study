package org.example.concurrency.chapter05.exam04;

public class ThreadNonSafeVariableExample {

    int sum = 0;

    public void printNumbers(int plus) {

        for (int i = 0; i <= 5; i++) {
            sum += i;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sum += plus;
        System.out.println(Thread.currentThread().getName() + " : " + sum);
    }

    public static void main(String[] args) {
        ThreadNonSafeVariableExample example = new ThreadNonSafeVariableExample();

        new Thread(() -> {
            example.printNumbers(50);

        }, "스레드1").start();

        new Thread(() -> {
            example.printNumbers(40);

        }, "스레드2").start();
    }
}
