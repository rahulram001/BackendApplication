package com.example.jobportal.service;

import com.example.jobportal.dto.JobDTO;
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
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    private JobDTO toDto(Job j) {
        JobDTO dto = new JobDTO();
        dto.setId(j.getId());
        dto.setTitle(j.getTitle());
        dto.setDescription(j.getDescription());
        dto.setRequirements(j.getRequirements());
        dto.setSalaryRange(j.getSalaryRange());
        dto.setSalary(j.getSalaryRange());
        dto.setEmployerId(j.getEmployer() != null ? j.getEmployer().getId() : null);
        dto.setCompany(j.getCompany());
        dto.setLocation(j.getLocation());
        dto.setType(j.getType());
        dto.setExperience(j.getExperience());
        dto.setBenefits(j.getBenefits());
        dto.setSkills(j.getSkills());
        dto.setApplicationDeadline(j.getApplicationDeadline());
        dto.setIsRemote(j.getIsRemote());
        dto.setStatus(j.getStatus());
        return dto;
    }

    private void apply(Job j, JobDTO dto) {
        j.setTitle(dto.getTitle());
        j.setDescription(dto.getDescription());
        j.setRequirements(dto.getRequirements());
        if (dto.getSalary() != null && !dto.getSalary().isEmpty()) {
            j.setSalaryRange(dto.getSalary());
        } else {
            j.setSalaryRange(dto.getSalaryRange());
        }
        j.setCompany(dto.getCompany());
        j.setLocation(dto.getLocation());
        j.setType(dto.getType());
        j.setExperience(dto.getExperience());
        j.setBenefits(dto.getBenefits());
        j.setSkills(dto.getSkills());
        j.setApplicationDeadline(dto.getApplicationDeadline());
        j.setIsRemote(dto.getIsRemote());
        j.setStatus(dto.getStatus());
        if (dto.getEmployerId() != null) {
            User employer = userRepository.findById(dto.getEmployerId())
                    .orElseThrow(() -> new CustomException("Employer not found", 404));
            j.setEmployer(employer);
        }
    }

    public JobDTO createJob(JobDTO dto) {
        System.out.println("JobService: createJob called with title=" + dto.getTitle() + ", company=" + dto.getCompany());
        Job j = new Job();
        apply(j, dto);
        j = jobRepository.save(j);
        System.out.println("JobService: job saved with id=" + j.getId());
        return toDto(j);
    }

    public JobDTO updateJob(Long id, JobDTO dto) {
        Job j = jobRepository.findById(id)
                .orElseThrow(() -> new CustomException("Job not found", 404));
        apply(j, dto);
        j = jobRepository.save(j);
        return toDto(j);
    }

    public List<JobDTO> listJobs() {
        return jobRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteJob(Long jobId) {
        System.out.println("JobService.deleteJob called with jobId: " + jobId);
        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
        System.out.println("JobService: deleted jobId " + jobId);
    }

    public List<JobDTO> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerId(employerId)
                .stream()
                .map(this::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    // Add this method for search endpoint
    public List<JobDTO> searchJobs(String q) {
        return jobRepository.findAll().stream()
                .filter(j -> j.getTitle() != null && j.getTitle().toLowerCase().contains(q.toLowerCase()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Add this method for dashboard stats endpoint
    public java.util.Map<String, Object> getDashboardStats() {
        long totalJobs = jobRepository.count();
        long activeJobs = jobRepository.findAll().stream().filter(j -> "active".equalsIgnoreCase(j.getStatus())).count();
        long closedJobs = jobRepository.findAll().stream().filter(j -> "closed".equalsIgnoreCase(j.getStatus())).count();
        return java.util.Map.of(
            "totalJobs", totalJobs,
            "activeJobs", activeJobs,
            "closedJobs", closedJobs
        );
    }
}


