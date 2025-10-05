package com.example.jobportal.controller;

import com.example.jobportal.dto.JobDTO;
import com.example.jobportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public JobDTO createJob(@RequestBody JobDTO jobDTO) {
        System.out.println("JobController: createJob called with title=" + jobDTO.getTitle() + ", company=" + jobDTO.getCompany());
        // Print all fields for debugging
        System.out.println("JobDTO: " +
            "location=" + jobDTO.getLocation() +
            ", type=" + jobDTO.getType() +
            ", experience=" + jobDTO.getExperience() +
            ", benefits=" + jobDTO.getBenefits() +
            ", skills=" + jobDTO.getSkills() +
            ", applicationDeadline=" + jobDTO.getApplicationDeadline() +
            ", isRemote=" + jobDTO.getIsRemote() +
            ", salary=" + jobDTO.getSalary() +
            ", salaryRange=" + jobDTO.getSalaryRange());
        return jobService.createJob(jobDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable(name = "id") Long id, @RequestBody JobDTO dto) {
        return ResponseEntity.ok(jobService.updateJob(id, dto));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable Long jobId) {
        System.out.println("Delete request for jobId: " + jobId);
        try {
            jobService.deleteJob(jobId);
            System.out.println("Job deleted: " + jobId);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to delete job: " + ex.getMessage()));
        }
    }

    @GetMapping
    public List<JobDTO> listJobs() {
        return jobService.listJobs();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobDTO>> searchJobs(@RequestParam String q) {
        return ResponseEntity.ok(jobService.searchJobs(q));
    }

    // Add this endpoint for dashboard statistics
    @GetMapping("/stats/dashboard")
    public ResponseEntity<java.util.Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(jobService.getDashboardStats());
    }

    // Example for endpoint using Long argument:
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<JobDTO>> getJobsByEmployer(@PathVariable(name = "employerId") Long employerId) {
        List<JobDTO> jobs = jobService.getJobsByEmployer(employerId);
        return ResponseEntity.ok(jobs);
    }
}
