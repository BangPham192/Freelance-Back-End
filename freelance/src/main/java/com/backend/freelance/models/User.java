package com.backend.freelance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity{

    @Column(name = "public_id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;

    private String username;
    private String email;
    private String password;

    @Column(name = "locked_at")
    public LocalDateTime lockedAt;

    @Column(name = "expired_at")
    public LocalDateTime expiredAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplications> jobApplications = new ArrayList<>();


}
