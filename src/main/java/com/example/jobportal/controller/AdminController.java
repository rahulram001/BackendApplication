package com.example.jobportal.controller;

import com.example.jobportal.dto.AdminDTO;
import com.example.jobportal.dto.UserDTO;
import com.example.jobportal.service.AdminService;
import com.example.jobportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String adminId = request.getOrDefault("adminId", "").trim();
        String email = request.getOrDefault("email", "").trim();
        String password = request.getOrDefault("password", "").trim();
        if (adminId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Admin ID, email, and password are required."
            ));
        }
        return ResponseEntity.ok(adminService.login(adminId, email, password));
    }

    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> generateReports() {
        return ResponseEntity.ok(Map.of("message", "Mock reports endpoint"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllRegisteredUsers() {
        List<UserDTO> users = userService.getAllUsers();
        // Debug: print user count and first user if present
        System.out.println("AdminController: getAllRegisteredUsers, count=" + users.size());
        if (!users.isEmpty()) {
            System.out.println("First user: " + users.get(0).getEmail());
        }
        return ResponseEntity.ok(users);
    }
}
