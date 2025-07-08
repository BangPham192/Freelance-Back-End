package com.backend.freelance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skills extends BaseEntity{

    @Column(name = "public_id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;
    private String name;
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSkills> jobSkills = new ArrayList<>();
}
