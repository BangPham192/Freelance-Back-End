package com.backend.freelance.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String requirements;

    @NotNull
    @NotBlank
    private String benefits;

    @NotNull
    private String budgetMin;
    @NotNull
    private String budgetMax;
    private List<UUID> skills; // List of skill public IDs to associate with the job
}
