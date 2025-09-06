package com.backend.freelance.dao;

public interface IRefreshTokenDao {
    String getRefreshTokenByUsername(String username);
    void storeRefreshToken(String username, String refreshToken);
    void deleteRefreshToken(String username);
}
