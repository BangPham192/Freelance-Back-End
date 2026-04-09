package com.backend.freelance.dao.impl;

import com.backend.freelance.dao.IPasswordResetTokenDao;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class PasswordResetTokenDaoImpl implements IPasswordResetTokenDao {

    private static final String KEY_PREFIX = "pwd_reset:";

    private final RedisTemplate<String, Object> redisTemplate;

    public PasswordResetTokenDaoImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void storeResetToken(String token, String email, long ttlSeconds) {
        redisTemplate.opsForValue().set(KEY_PREFIX + token, email, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getEmailByToken(String token) {
        Object value = redisTemplate.opsForValue().get(KEY_PREFIX + token);
        return value != null ? value.toString() : null;
    }

    @Override
    public void deleteResetToken(String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }
}

