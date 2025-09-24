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
    public ResponseEntity<ApplicationDTO> apply(@RequestBody ApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.applyJob(dto));
    }

    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<List<ApplicationDTO>> viewForSeeker(@PathVariable Long seekerId) {
        return ResponseEntity.ok(applicationService.viewApplicationsForSeeker(seekerId));
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<ApplicationDTO>> viewForEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(applicationService.viewApplicationsForEmployer(employerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}
