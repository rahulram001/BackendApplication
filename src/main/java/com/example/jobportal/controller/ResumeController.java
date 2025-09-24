package com.example.jobportal.controller;

import com.example.jobportal.dto.ResumeDTO;
import com.example.jobportal.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeDTO> upload(@RequestBody ResumeDTO dto) {
        return ResponseEntity.ok(resumeService.uploadResume(dto));
    }

    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<List<ResumeDTO>> list(@PathVariable Long seekerId) {
        return ResponseEntity.ok(resumeService.listResumes(seekerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}
