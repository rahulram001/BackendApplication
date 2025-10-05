package com.example.jobportal.controller;

import com.example.jobportal.dto.EmployeeDTO;
import com.example.jobportal.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.getOrDefault("email", "").trim();
        String password = request.getOrDefault("password", "").trim();
        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Email and password are required."
            ));
        }
        return ResponseEntity.ok(employeeService.login(email, password));
    }
}
