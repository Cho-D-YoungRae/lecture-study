package org.example.concurrency.chapter01.exam01;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ParallelismExample {

    private static final Logger log = LoggerFactory.getLogger(ParallelismExample.class);

    public static void main(String[] args) {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        log.info("CPU 개수: {}", cpuCores);

        // CPU 개수만큼 데이터를 생성
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < cpuCores; i++) {
            data.add(i);
        }

        // CPU 개수만큼 데이터를 병렬로 처리
        long startTime1 = System.currentTimeMillis();
        long sum1 = data
                .stream()
                .parallel()
                .mapToLong(i -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return (long) i * i;
                })
                .sum();
        long endTime1 = System.currentTimeMillis();

        log.info("처리 시간: {}ms", endTime1 - startTime1);
        log.info("결과 1: {}", sum1);
    }
}
