package com.backend.freelance.dtos;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AuthTokenDto {
    public String accessToken;
    public String refreshToken;
    public LocalDateTime accessTokenExpiresAt;
    public LocalDateTime refreshTokenExpiresAt;
}
