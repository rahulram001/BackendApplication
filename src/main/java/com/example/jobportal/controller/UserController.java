package com.example.jobportal.controller;

import com.example.jobportal.dto.UserDTO;
import com.example.jobportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/seeker/register")
    public ResponseEntity<Map<String, Object>> register(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("phone") String phone,
        @RequestParam("location") String location,
        @RequestParam("experience") String experience,
        @RequestParam("skills") String skills,
        @RequestParam(value = "resume", required = false) MultipartFile resume
    ) {
        System.out.println("Register request: firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", location=" + location + ", experience=" + experience + ", skills=" + skills + ", resume=" + (resume != null ? resume.getOriginalFilename() : "null"));
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "First name, last name, email, and password are required."
            ));
        }

        String resumePath = "";
        if (resume != null && !resume.isEmpty()) {
            resumePath = resume.getOriginalFilename(); // Placeholder, replace with actual storage logic
        }

        return ResponseEntity.ok(userService.register(
            firstName, lastName, email, password, phone, location, experience, skills, resumePath
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
    public ResponseEntity<UserDTO> getProfile(@PathVariable(name = "id") Long id) {
        UserDTO dto = userService.getProfile(id);
        // Debug: print all fields to verify
        System.out.println("Profile loaded: name=" + dto.getName() +
            ", email=" + dto.getEmail() +
            ", phone=" + dto.getPhone() +
            ", location=" + dto.getLocation() +
            ", experience=" + dto.getExperience() +
            ", skills=" + dto.getSkills() +
            ", resume=" + dto.getResume());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable(name = "id") Long id, @RequestBody UserDTO update) {
        return ResponseEntity.ok(userService.updateProfile(id, update));
    }
}



