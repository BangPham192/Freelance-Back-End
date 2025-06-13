package com.backend.freelance.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID publicId;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
}
