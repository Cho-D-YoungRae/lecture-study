package org.example.concurrency.chapter04.exam04;

public class ThreadGroupScopeExample {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup topGroup = new ThreadGroup("TopGroup");
        ThreadGroup subGroup = new ThreadGroup(topGroup, "SubGroup");

        Thread 상위_스레드_그룹 = new Thread(topGroup, () -> {
            System.out.println("상위 그룹 스레드에서 하위 그룹의 최대 우선 순위 설정 변경 전 : " + subGroup.getMaxPriority());
            subGroup.setMaxPriority(7);
            System.out.println("상위 그룹 스레드에서 하위 그룹의 최대 우선 순위 설정 변경 후 : " + subGroup.getMaxPriority());
        }, "상위 스레드 그룹");

        Thread 하위_스레드_그룹 = new Thread(subGroup, () -> {
            System.out.println("하위 그룹 스레드에서 상위 그룹의 최대 우선 순위 설정 변경 전 : " + topGroup.getMaxPriority());
            topGroup.setMaxPriority(4);
            System.out.println("하위 그룹 스레드에서 상위 그룹의 최대 우선 순위 설정 변경 후 : " + topGroup.getMaxPriority());
        }, "하위 스레드 그룹");

        상위_스레드_그룹.start();
        하위_스레드_그룹.start();

        상위_스레드_그룹.join();
        하위_스레드_그룹.join();

        System.out.println(상위_스레드_그룹.getName() + " : " + 상위_스레드_그룹.getPriority());
        System.out.println(하위_스레드_그룹.getName() + " : " + 하위_스레드_그룹.getPriority());

        Thread userThread1 = new Thread(topGroup, () -> {}, "유저 스레드 1");
        Thread userThread2 = new Thread(topGroup, () -> {}, "유저 스레드 2");

        userThread1.start();
        userThread2.start();

        userThread1.join();
        userThread2.join();

        System.out.println(userThread1.getName() + " : " + userThread1.getPriority());
        System.out.println(userThread2.getName() + " : " + userThread2.getPriority());
    }
}
