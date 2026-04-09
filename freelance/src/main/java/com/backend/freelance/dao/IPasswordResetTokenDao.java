package com.backend.freelance.dao;

public interface IPasswordResetTokenDao {
    void storeResetToken(String token, String email, long ttlSeconds);
    String getEmailByToken(String token);
    void deleteResetToken(String token);
}

