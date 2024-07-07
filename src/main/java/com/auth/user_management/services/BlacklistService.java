package com.auth.user_management.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlacklistService {
    private final RedisTemplate<String, String> redisTemplate;

    public BlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "blacklisted", 30, TimeUnit.DAYS);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
