package com.example.jobportal.service;

import com.example.jobportal.dto.ResumeDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {
    public ResumeDTO uploadResume(ResumeDTO dto) {
        dto.setId(1L);
        dto.setUploadedAt(Instant.now());
        return dto;
    }

    public List<ResumeDTO> listResumes(Long seekerId) {
        return new ArrayList<>();
    }

    public void deleteResume(Long id) {
    }
}
