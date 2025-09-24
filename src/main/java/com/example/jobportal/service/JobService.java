package com.example.jobportal.service;

import com.example.jobportal.dto.JobDTO;
import com.example.jobportal.exception.CustomException;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    private JobDTO toDto(Job j) {
        JobDTO dto = new JobDTO();
        dto.setId(j.getId());
        dto.setTitle(j.getTitle());
        dto.setDescription(j.getDescription());
        dto.setRequirements(j.getRequirements());
        dto.setSalaryRange(j.getSalaryRange());
        dto.setEmployerId(j.getEmployer() != null ? j.getEmployer().getId() : null);
        return dto;
    }

    private void apply(Job j, JobDTO dto) {
        j.setTitle(dto.getTitle());
        j.setDescription(dto.getDescription());
        j.setRequirements(dto.getRequirements());
        j.setSalaryRange(dto.getSalaryRange());
        if (dto.getEmployerId() != null) {
            User employer = userRepository.findById(dto.getEmployerId())
                    .orElseThrow(() -> new CustomException("Employer not found", 404));
            j.setEmployer(employer);
        }
    }

    public JobDTO createJob(JobDTO dto) {
        Job j = new Job();
        apply(j, dto);
        j = jobRepository.save(j);
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

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id))
            throw new CustomException("Job not found", 404);
        jobRepository.deleteById(id);
    }

    public List<JobDTO> searchJobs(String keyword) {
        String kw = keyword == null ? "" : keyword.toLowerCase();
        return jobRepository.findAll().stream()
                .filter(j -> (j.getTitle() != null && j.getTitle().toLowerCase().contains(kw)) ||
                        (j.getDescription() != null && j.getDescription().toLowerCase().contains(kw)) ||
                        (j.getRequirements() != null && j.getRequirements().toLowerCase().contains(kw)))
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
