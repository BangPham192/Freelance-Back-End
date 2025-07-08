package com.backend.freelance.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplyRequest {
    private String coverLetter;
    private String expectedFee;
    private String estimatedTime;

}
