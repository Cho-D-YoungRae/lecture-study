package org.example.concurrency.chapter04.exam05;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadLocalLogger {

    private static final ThreadLocal<List<String>> THREAD_LOG = ThreadLocal.withInitial(ArrayList::new);

    public static void add(String log) {
        THREAD_LOG.get().add(log);
    }

    public static void print() {
        System.out.println("[" + Thread.currentThread().getName() + "]: " + String.join(" ->", THREAD_LOG.get()));
    }

    public static void clear() {
        THREAD_LOG.remove();
    }

    static class ServiceA {
        public void process() {
            ThreadLocalLogger.add("ServiceA 로직 수행");
        }
    }

    static class ServiceB {
        public void process() {
            ThreadLocalLogger.add("ServiceB 로직 수행");
        }
    }

    static class ServiceC {
        public void process() {
            ThreadLocalLogger.add("ServiceC 로직 수행");
        }
    }
}
