package com.example.jobportal.service;

import com.example.jobportal.dto.ApplicationDTO;
import com.example.jobportal.exception.CustomException;
import com.example.jobportal.model.Application;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository,
            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    private ApplicationDTO toDto(Application a) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(a.getId());
        dto.setJobId(a.getJob() != null ? a.getJob().getId() : null);
        dto.setSeekerId(a.getSeeker() != null ? a.getSeeker().getId() : null);
        dto.setStatus(a.getStatus().name());
        return dto;
    }

    public ApplicationDTO applyJob(ApplicationDTO dto) {
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new CustomException("Job not found", 404));
        User seeker = userRepository.findById(dto.getSeekerId())
                .orElseThrow(() -> new CustomException("Seeker not found", 404));
        Application a = new Application();
        a.setJob(job);
        a.setSeeker(seeker);
        a.setStatus(Application.Status.APPLIED);
        a = applicationRepository.save(a);
        return toDto(a);
    }

    public List<ApplicationDTO> viewApplicationsForSeeker(Long seekerId) {
        return applicationRepository.findAll().stream()
                .filter(a -> a.getSeeker() != null && a.getSeeker().getId().equals(seekerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> viewApplicationsForEmployer(Long employerId) {
        return applicationRepository.findAll().stream()
                .filter(a -> a.getJob() != null &&
                        a.getJob().getEmployer() != null &&
                        a.getJob().getEmployer().getId().equals(employerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ApplicationDTO updateStatus(Long id, String status) {
        Application a = applicationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Application not found", 404));
        try {
            a.setStatus(Application.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Invalid status. Use APPLIED/REVIEWED/ACCEPTED/REJECTED", 400);
        }
        a = applicationRepository.save(a);
        return toDto(a);
    }
}
