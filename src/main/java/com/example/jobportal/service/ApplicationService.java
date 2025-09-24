package com.example.jobportal.service;

import com.example.jobportal.dto.ApplicationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    public ApplicationDTO applyJob(ApplicationDTO dto) {
        dto.setId(1L);
        dto.setStatus("APPLIED");
        return dto;
    }

    public List<ApplicationDTO> viewApplicationsForSeeker(Long seekerId) {
        return new ArrayList<>();
    }

    public List<ApplicationDTO> viewApplicationsForEmployer(Long employerId) {
        return new ArrayList<>();
    }

    public ApplicationDTO updateStatus(Long id, String status) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(id);
        dto.setStatus(status);
        return dto;
    }
}
