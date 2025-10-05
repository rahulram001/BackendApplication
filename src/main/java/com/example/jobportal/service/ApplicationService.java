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
        dto.setStatus(a.getStatus() != null ? a.getStatus().name() : "");
        if (a.getJob() != null) {
            dto.setJobTitle(a.getJob().getTitle() != null ? a.getJob().getTitle() : "");
            dto.setCompany(a.getJob().getCompany() != null ? a.getJob().getCompany() : "");
            dto.setLocation(a.getJob().getLocation() != null ? a.getJob().getLocation() : "");
            dto.setSalary(
                a.getJob().getSalaryRange() != null ? a.getJob().getSalaryRange() :
                (a.getJob().getSalary() != null ? a.getJob().getSalary() : "")
            );
            dto.setType(a.getJob().getType() != null ? a.getJob().getType() : "");
            dto.setRequirements(a.getJob().getRequirements() != null ? a.getJob().getRequirements() : "");
        }
        if (a.getSeeker() != null) {
            dto.setCandidateName(a.getSeeker().getName() != null ? a.getSeeker().getName() : "");
            dto.setCandidateEmail(a.getSeeker().getEmail() != null ? a.getSeeker().getEmail() : "");
            dto.setCandidatePhone(a.getSeeker().getPhone() != null ? a.getSeeker().getPhone() : "");
            dto.setExperience(a.getSeeker().getExperience() != null ? a.getSeeker().getExperience() : "");
            dto.setSkills(a.getSeeker().getSkills() != null ? a.getSeeker().getSkills() : "");
            dto.setResume(a.getSeeker().getResume() != null ? a.getSeeker().getResume() : "");
        }
        // Optionally set appliedDate, coverLetter, etc.
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
        // Optionally set appliedDate, coverLetter, etc.
        a = applicationRepository.save(a);
        return toDto(a);
    }

    public List<ApplicationDTO> viewApplicationsForSeeker(Long seekerId) {
        if (seekerId == null) {
            throw new CustomException("Seeker ID is required", 400);
        }
        return applicationRepository.findAll().stream()
                .filter(a -> a.getSeeker() != null && a.getSeeker().getId() != null && a.getSeeker().getId().equals(seekerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> viewApplicationsForEmployer(Long employerId) {
        return applicationRepository.findAll().stream()
                .filter(a -> {
                    if (a.getJob() == null || a.getJob().getEmployer() == null) return false;
                    Long jobEmployerId = a.getJob().getEmployer().getId();
                    System.out.println("Application id=" + a.getId() +
                        ", jobId=" + (a.getJob() != null ? a.getJob().getId() : "null") +
                        ", jobEmployerId=" + jobEmployerId +
                        ", filter employerId=" + employerId);
                    return jobEmployerId != null && jobEmployerId.equals(employerId);
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ApplicationDTO updateStatus(Long id, String status) {
        Application a = applicationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Application not found", 404));
        try {
            a.setStatus(Application.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Invalid status. Use APPLIED/UNDER_REVIEW/INTERVIEW/ACCEPTED/REJECTED", 400);
        }
        a = applicationRepository.save(a);
        return toDto(a);
    }

    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
    // --- ENDPOINTS MAPPED TO THESE SERVICE METHODS ---
    // POST   /api/applications                -> applyJob(ApplicationDTO dto)
    // GET    /api/applications/seeker/{id}    -> viewApplicationsForSeeker(Long seekerId)
    // GET    /api/applications/employer/{id}  -> viewApplicationsForEmployer(Long employerId)
    // PUT    /api/applications/{id}/status    -> updateStatus(Long id, String status)
    // GET    /api/applications                -> getAllApplications() (for admin/all)
}


