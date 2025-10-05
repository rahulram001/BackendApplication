package com.example.jobportal.controller;

import com.example.jobportal.dto.ApplicationDTO;
import com.example.jobportal.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationDTO> applyJob(@RequestBody ApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.applyJob(dto));
    }

    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsForSeeker(@PathVariable(name = "seekerId") Long seekerId) {
        return ResponseEntity.ok(applicationService.viewApplicationsForSeeker(seekerId));
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsForEmployer(@PathVariable(name = "employerId") Long employerId) {
        List<ApplicationDTO> applications = applicationService.viewApplicationsForEmployer(employerId);
        // Debug: print applications to backend console
        System.out.println("Applications for employerId=" + employerId + ": " + applications.size());
        for (ApplicationDTO app : applications) {
            System.out.println("  Application id=" + app.getId() + ", jobId=" + app.getJobId() + ", seekerId=" + app.getSeekerId() + ", status=" + app.getStatus());
        }
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationDTO> updateStatus(@PathVariable(name = "id") Long id, @RequestBody java.util.Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdrawApplication(@PathVariable(name = "id") Long id) {
        // Implement withdraw logic if needed, or just delete
        // applicationService.withdrawApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
}

