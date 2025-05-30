package com.example.parallel.forkjoin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.util.MyLogger.log;

public class CompletableFutureMain {

    public static void main(String[] args) {
        CompletableFuture.runAsync(() -> log("Fork/Join")); // Fork/Join 공용 풀

        ExecutorService es = Executors.newFixedThreadPool(100);
        CompletableFuture.runAsync(() -> log("Custom Pool"), es); // 별도 풀
        es.close();
    }
}
