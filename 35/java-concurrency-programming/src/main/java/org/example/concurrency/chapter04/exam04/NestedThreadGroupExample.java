package org.example.concurrency.chapter04.exam04;

public class NestedThreadGroupExample {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup topGroup = new ThreadGroup("TopGroup");
        ThreadGroup subGroup = new ThreadGroup(topGroup, "SubGroup");

        final Thread topGroupThread = new Thread(topGroup, new GroupRunnable(), "TopGroupThread");
        final Thread subGroupThread = new Thread(subGroup, new GroupRunnable(), "SubGroupThread");

        topGroupThread.start();
        subGroupThread.start();

        topGroupThread.join();
        subGroupThread.join();

        System.out.println("-".repeat(50));
        System.out.println("최상위 스레드 그룹의 정보");
        topGroup.list();
    }

    static class GroupRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " >>> " + Thread.currentThread().getThreadGroup().getName());
        }
    }
}
