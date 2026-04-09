package com.backend.freelance.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionMessage {
    private String timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
