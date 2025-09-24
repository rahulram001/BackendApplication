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

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(userService.register(
                request.getOrDefault("name", ""),
                request.getOrDefault("email", ""),
                request.getOrDefault("password", ""),
                request.getOrDefault("role", "SEEKER")));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(userService.login(
                request.getOrDefault("email", ""),
                request.getOrDefault("password", "")));
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
