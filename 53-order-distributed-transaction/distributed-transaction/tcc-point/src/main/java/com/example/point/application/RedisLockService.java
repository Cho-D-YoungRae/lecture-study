package com.example.point.application;

import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RedisLockService {

    private static final Logger log = getLogger(RedisLockService.class);

    private final StringRedisTemplate stringRedisTemplate;

    public RedisLockService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean tryLock(String key, String value) {
        log.info("try lock for key: {}, value: {}", key, value);
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().setIfAbsent(key, value));
    }

    public void releaseLock(String key) {
        log.info("release lock for key: {}", key);
        stringRedisTemplate.delete(key);
    }
}
