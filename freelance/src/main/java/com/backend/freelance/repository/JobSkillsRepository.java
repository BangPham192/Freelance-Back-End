package com.backend.freelance.repository;

import com.backend.freelance.models.JobSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface JobSkillsRepository extends JpaRepository<JobSkills, Long> {
    List<JobSkills> findAllByJobPublicId(UUID jobPublicId);
}
