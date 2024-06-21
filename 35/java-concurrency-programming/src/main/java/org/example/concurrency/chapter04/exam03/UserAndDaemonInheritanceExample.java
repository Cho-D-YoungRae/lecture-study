package org.example.concurrency.chapter04.exam03;

public class UserAndDaemonInheritanceExample {

    public static void main(String[] args) {

        Thread userThread = new Thread(() -> {
            Thread parentThread = Thread.currentThread();
            System.out.println("[" + parentThread.getName() + "] 데몬 상태: " + Thread.currentThread().isDaemon());
            new Thread(() -> {
                System.out.println("[" + parentThread.getName() + "] 자식 데몬 상태: " + Thread.currentThread().isDaemon());
            }).start();
        });
        userThread.setName("사용자 스레드");

        Thread daemonThread = new Thread(() -> {
            Thread parentThread = Thread.currentThread();
            System.out.println("[" + parentThread.getName() + "] 데몬 상태: " + Thread.currentThread().isDaemon());
            new Thread(() -> {
                System.out.println("[" + parentThread.getName() + "] 자식 데몬 상태: " + Thread.currentThread().isDaemon());
            }).start();
        });
        daemonThread.setName("데몬 스레드");
        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();
    }
}
