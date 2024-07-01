package org.example.concurrency.chapter05.exam04;

public class ThreadSafeLocalVariableExample {

    public void printNumbers(int plus) {
        // 지역 변수, 매개 변수로 정의된 변수. 각 스레드는 이 변수의 독립된 복사본을 가짐.
        int localSum = 0;

        for (int i = 0; i <= 5; i++) {
            localSum += i;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        localSum += plus;
        System.out.println(Thread.currentThread().getName() + " : " + localSum);
    }

    public static void main(String[] args) {
        ThreadSafeLocalVariableExample example = new ThreadSafeLocalVariableExample();

        new Thread(() -> {
            example.printNumbers(50);

        }, "스레드1").start();

        new Thread(() -> {
            example.printNumbers(40);

        }, "스레드2").start();
    }
}
