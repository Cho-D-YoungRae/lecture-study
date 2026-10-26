package org.example.concurrency.chapter04.exam04;

public class ThreadGroupExample {

    public static void main(String[] args) {
        ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup customThreadGroup = new ThreadGroup("CustomThreadGroup");

        Thread defaultGroupThread = new Thread(new GroupRunnable(), "DefaultGroupThread");
        Thread mainGroupThread = new Thread(mainThreadGroup, new GroupRunnable(), "MainGroupThread");
        Thread customGroupThread = new Thread(customThreadGroup, new GroupRunnable(), "CustomGroupThread");

        defaultGroupThread.start();
        mainGroupThread.start();
        customGroupThread.start();
    }

    static class GroupRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " >>> " + Thread.currentThread().getThreadGroup().getName());
        }
    }
}
