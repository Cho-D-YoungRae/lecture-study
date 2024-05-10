package com.example.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppliedUserRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long add(final Long userId) {
        return redisTemplate
                .opsForSet()
                .add("applied_users", userId.toString());
    }
}
