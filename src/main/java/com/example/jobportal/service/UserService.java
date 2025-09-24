package com.example.jobportal.service;

import com.example.jobportal.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    public Map<String, Object> register(String name, String email, String password, String role) {
        return Map.of(
                "message", "User registered successfully",
                "user", new UserDTO() {
                    {
                        setId(1L);
                        setName(name);
                        setEmail(email);
                        setRole(role);
                    }
                });
    }

    public Map<String, Object> login(String email, String password) {
        return Map.of(
                "token", "mock-jwt-token",
                "user", new UserDTO() {
                    {
                        setId(1L);
                        setName("Demo User");
                        setEmail(email);
                        setRole("SEEKER");
                    }
                });
    }

    public UserDTO getProfile(Long userId) {
        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setName("Demo User");
        dto.setEmail("demo@example.com");
        dto.setRole("SEEKER");
        return dto;
    }

    public UserDTO updateProfile(Long userId, UserDTO update) {
        update.setId(userId);
        return update;
    }
}
