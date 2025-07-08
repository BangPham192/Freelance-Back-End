package com.backend.freelance.repository;

import com.backend.freelance.models.JobApplications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobApplicationsRepository extends JpaRepository<JobApplications, UUID> {
    JobApplications findByPublicId(UUID publicId);
    Page<JobApplications> findAllByFreelancerId(Long freelancerId, Pageable pageable);
}
