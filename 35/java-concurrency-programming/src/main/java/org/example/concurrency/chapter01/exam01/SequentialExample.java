package org.example.concurrency.chapter01.exam01;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SequentialExample {

    private static final Logger log = LoggerFactory.getLogger(SequentialExample.class);

    public static void main(String[] args) {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        log.info("CPU 개수: {}", cpuCores);

        // CPU 개수만큼 데이터를 생성
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < cpuCores; i++) {
            data.add(i);
        }

        // CPU 개수만큼 데이터를 순차적 처리
        long startTime = System.currentTimeMillis();
        long sum = data
                .stream()
                .mapToLong(i -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return (long) i * i;
                })
                .sum();
        long endTime = System.currentTimeMillis();

        log.info("처리 시간: {}ms", endTime - startTime);
        log.info("결과: {}", sum);
    }
}
