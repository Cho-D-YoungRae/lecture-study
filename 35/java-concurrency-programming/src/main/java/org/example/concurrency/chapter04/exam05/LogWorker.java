package org.example.concurrency.chapter04.exam05;

public class LogWorker implements Runnable {

    @Override
    public void run() {
        ThreadLocalLogger.ServiceA serviceA = new ThreadLocalLogger.ServiceA();
        ThreadLocalLogger.ServiceB serviceB = new ThreadLocalLogger.ServiceB();
        ThreadLocalLogger.ServiceC serviceC = new ThreadLocalLogger.ServiceC();

        if ("Thread-1".equals(Thread.currentThread().getName())) {
            serviceA.process();
            serviceB.process();
            serviceC.process();
        } else if ("Thread-2".equals(Thread.currentThread().getName())) {
            serviceB.process();
            serviceA.process();
            serviceC.process();
        } else {
            serviceC.process();
            serviceA.process();
            serviceB.process();
        }

        ThreadLocalLogger.print();
        ThreadLocalLogger.clear();
    }
}
