package com.backend.freelance.repository;

import com.backend.freelance.models.Jobs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, UUID> {
    Jobs findByPublicId(UUID publicId);
    Page<Jobs> findAll(Pageable pageable);
}
