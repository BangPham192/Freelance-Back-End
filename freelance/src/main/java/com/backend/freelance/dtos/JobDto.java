package com.backend.freelance.dtos;

import com.backend.freelance.models.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private UUID id;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private JobStatus status;
    private LocalDateTime createdAt;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private List<String> skills;
    private String clientName;
    private List<JobApplicationDto> jobApplications;
}
