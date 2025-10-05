package com.example.jobportal.service;

import com.example.jobportal.dto.AdminDTO;
import com.example.jobportal.model.Admin;
import com.example.jobportal.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Map<String, Object> login(String adminId, String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByAdminId(adminId);
        if (adminOpt.isEmpty() || !adminOpt.get().getEmail().equals(email) || !adminOpt.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid admin credentials");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("user", toDto(adminOpt.get()));
        result.put("role", "admin");
        return result;
    }

    public AdminDTO toDto(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setAdminId(admin.getAdminId());
        dto.setEmail(admin.getEmail());
        dto.setPassword(admin.getPassword());
        return dto;
    }
}
