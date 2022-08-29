package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.facade.LettuceLockStockFacade;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LettuceLockStockServiceTest {

    @Autowired
    private LettuceLockStockFacade stockService;

    @Autowired
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    void beforeEach() {
        this.stock = stockRepository.save(Stock.builder()
                .productId(1L)
                .quantity(100L)
                .build());
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    void 동시에_100개의_요청() throws Exception {
        int threadCount = 100;
        // 멀티스레드 비동기로 실행하는 작업을 단순화하여 사용할 있도록 도와주는 JAVA API
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        // 100개의 요청이 끝날 때 까지 기다려야 하므로 CountDownLatch 사용
        // 다른 쓰레드에서 실행 중인 작업이 끝날 때까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(this.stock.getId(), 1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(this.stock.getId()).orElseThrow();

        assertThat(stock.getQuantity()).isZero();
    }
}