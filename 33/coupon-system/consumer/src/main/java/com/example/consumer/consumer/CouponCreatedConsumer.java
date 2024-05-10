package com.example.consumer.consumer;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(final Long userId) {
        try {
            couponRepository.save(new Coupon(userId));
        } catch (final Exception e) {
            log.error("failed to create coupon:: userId={}", userId);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
