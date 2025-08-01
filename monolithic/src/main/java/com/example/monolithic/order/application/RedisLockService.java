package com.example.monolithic.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
