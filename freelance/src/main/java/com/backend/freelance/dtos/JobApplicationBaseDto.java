package com.backend.freelance.dtos;

import com.backend.freelance.models.JobApplicationsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationBaseDto {
    private String coverLetter;
    private BigDecimal expectedFee;
    private int estimatedTime;
    private String attachmentUrl;
    private JobApplicationsStatus applicationsStatus;
    private LocalDateTime createdAt;
}
