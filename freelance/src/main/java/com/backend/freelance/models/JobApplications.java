package com.backend.freelance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
public class JobApplications extends BaseEntity{

    @Column(name = "public_id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;

    @Column(name = "cover_letter")
    private String coverLetter;

    @Column(name = "expected_fee", precision = 19, scale = 2)
    private BigDecimal expectedFee;

    @Column(name = "estimated_time")
    private int estimatedTime;

    @Enumerated(EnumType.STRING)
    private JobApplicationsStatus status;

    private String attachmentUrl;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Jobs job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id")
    private User freelancer;
}
