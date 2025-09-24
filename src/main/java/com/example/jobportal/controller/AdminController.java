package com.example.jobportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/manage-users")
    public ResponseEntity<Map<String, Object>> manageUsers() {
        return ResponseEntity.ok(Map.of("message", "Mock manage users endpoint"));
    }

    @GetMapping("/manage-jobs")
    public ResponseEntity<Map<String, Object>> manageJobs() {
        return ResponseEntity.ok(Map.of("message", "Mock manage jobs endpoint"));
    }

    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> generateReports() {
        return ResponseEntity.ok(Map.of("message", "Mock reports endpoint"));
    }
}
