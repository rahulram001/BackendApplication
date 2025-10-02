package com.example.jobportal.controller;

import com.example.jobportal.dto.UserDTO;
import com.example.jobportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/seeker/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        System.out.println("Register request: " + request); // Debug log
        String firstName = request.getOrDefault("firstName", "").trim();
        String lastName = request.getOrDefault("lastName", "").trim();
        String email = request.getOrDefault("email", "").trim();
        String password = request.getOrDefault("password", "").trim();
        String phone = request.getOrDefault("phone", "").trim();
        String location = request.getOrDefault("location", "").trim();
        String experience = request.getOrDefault("experience", "").trim();
        String skills = request.getOrDefault("skills", "").trim();
        String resume = request.getOrDefault("resume", "").trim();

        if (firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "First name, email, and password are required."
            ));
        }

        return ResponseEntity.ok(userService.register(
            firstName, lastName, email, password, phone, location, experience, skills, resume
        ));
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

        return ResponseEntity.ok(userService.login(email, password));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Long id, @RequestBody UserDTO update) {
        return ResponseEntity.ok(userService.updateProfile(id, update));
    }
}

