package com.backend.freelance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "job_skills", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "skill_id"}))
@Getter
@Setter
public class JobSkills extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Jobs job;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id" , nullable = false)
    private Skills skill;
}
