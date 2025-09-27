package com.backend.freelance.repository;

import com.backend.freelance.models.JobApplications;
import com.backend.freelance.models.JobApplicationsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobApplicationsRepository extends JpaRepository<JobApplications, UUID> {
    JobApplications findByPublicId(UUID publicId);

    @Query("SELECT ja FROM JobApplications ja WHERE ja.freelancer.id = :freelancerId" +
    " AND ja.status IN :statuses")
    Page<JobApplications> findAllByFreelancerIdAndStatusIn(Long freelancerId, List<JobApplicationsStatus> statuses, Pageable pageable);

    List<JobApplications> findAllByJobIdAndStatusIn(Long jobId, List<JobApplicationsStatus> statuses);
}
