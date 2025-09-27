package com.backend.freelance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Jobs extends BaseEntity{
    @Column (name = "public_id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    @Column(name = "requirements", columnDefinition = "LONGTEXT", nullable = false)
    private String requirements;

    @Column(name = "benefits" , columnDefinition = "LONGTEXT", nullable = false)
    private String benefits;

    @Column(name = "budget_min")
    private BigDecimal budgetMin;
    @Column(name = "budget_max")
    private BigDecimal budgetMax;
    @Column(name = "client_id", insertable = false, updatable = false)
    private Long clientId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSkills> jobSkills = new ArrayList<>();
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplications> jobApplications = new ArrayList<>();

}
