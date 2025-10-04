package com.backend.freelance.repository;

import com.backend.freelance.models.JobStatus;
import com.backend.freelance.models.Jobs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, UUID> {
    Jobs findByPublicId(UUID publicId);
    Page<Jobs> findAll(Pageable pageable);

    @Query("SELECT j FROM Jobs j WHERE j.status = :status")
    Page<Jobs> findAllByStatus(@Param("status") JobStatus status, Pageable pageable);
}
