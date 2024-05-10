package com.example.api.service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public void apply(final Long userId) {
        final Long applied = appliedUserRepository.add(userId);
        if (applied != 1) {
            return;
        }

        final long count = couponCountRepository.increase();

        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }
}
