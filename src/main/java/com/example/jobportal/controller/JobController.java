package com.example.jobportal.controller;

import com.example.jobportal.dto.JobDTO;
import com.example.jobportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobDTO dto) {
        return ResponseEntity.ok(jobService.updateJob(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> listJobs() {
        return ResponseEntity.ok(jobService.listJobs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobDTO>> searchJobs(@RequestParam String q) {
        return ResponseEntity.ok(jobService.searchJobs(q));
    }
}
