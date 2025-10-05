package com.example.jobportal.repository;

import com.example.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // Remove countByStatus if not used, or if you use it, make sure you pass Application.Status enum
    // For dashboard stats, just use count()
}
    // If you are not using this method, you can remove it.
