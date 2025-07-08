package com.backend.freelance.repository;

import com.backend.freelance.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface SkillsRepository extends JpaRepository<Skills, UUID> {
    List<Skills> findAllByPublicIdIn(List<UUID> publicId);

    boolean existsByPublicId(UUID publicId);
}
