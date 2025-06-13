package com.backend.freelance.repository;

import com.backend.freelance.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRole, Long> {
}
