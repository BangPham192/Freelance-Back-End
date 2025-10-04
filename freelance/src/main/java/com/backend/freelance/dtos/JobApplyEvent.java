package com.backend.freelance.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplyEvent {
    private String jobName;
    private String freelancerEmail;
    private String clientEmail;
    private String subject;
    private String body;
    private String attachmentUrl;
}
