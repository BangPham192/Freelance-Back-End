package com.backend.freelance.dao.impl;

import com.backend.freelance.dao.IRefreshTokenDao;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenDaoImpl implements IRefreshTokenDao {
    private static final String HASH_REFERENCE = "refresh_token";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOps;

    @Override
    public String getRefreshTokenByUsername(String username) {
        return hashOps.get(HASH_REFERENCE, username);
    }

    @Override
    public void storeRefreshToken(String username, String refreshToken) {
        hashOps.put(HASH_REFERENCE, username, refreshToken);
    }

    @Override
    public void deleteRefreshToken(String username) {
        hashOps.delete(HASH_REFERENCE, username);
    }
}
