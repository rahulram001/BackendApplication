package com.example.jobportal.service;

import com.example.jobportal.dto.ResumeDTO;
import com.example.jobportal.exception.CustomException;
import com.example.jobportal.model.Resume;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.ResumeRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    private ResumeDTO toDto(Resume r) {
        ResumeDTO dto = new ResumeDTO();
        dto.setId(r.getId());
        dto.setSeekerId(r.getSeeker() != null ? r.getSeeker().getId() : null);
        dto.setFileUrl(r.getFileUrl());
        dto.setUploadedAt(r.getUploadedAt());
        return dto;
    }

    public ResumeDTO uploadResume(ResumeDTO dto) {
        User seeker = userRepository.findById(dto.getSeekerId())
                .orElseThrow(() -> new CustomException("Seeker not found", 404));
        Resume r = new Resume();
        r.setSeeker(seeker);
        r.setFileUrl(dto.getFileUrl());
        r.setUploadedAt(Instant.now());
        r = resumeRepository.save(r);
        return toDto(r);
    }

    public List<ResumeDTO> listResumes(Long seekerId) {
        return resumeRepository.findAll().stream()
                .filter(r -> r.getSeeker() != null && r.getSeeker().getId().equals(seekerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteResume(Long id) {
        if (!resumeRepository.existsById(id))
            throw new CustomException("Resume not found", 404);
        resumeRepository.deleteById(id);
    }
}