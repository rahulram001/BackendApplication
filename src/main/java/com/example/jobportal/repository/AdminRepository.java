package com.example.jobportal.repository;

import com.example.jobportal.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminId(String adminId);
    Optional<Admin> findByEmail(String email);
}
