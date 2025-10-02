package com.example.jobportal.service;

import com.example.jobportal.dto.UserDTO;
import com.example.jobportal.exception.CustomException;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole().name());
        return dto;
    }

    public Map<String, Object> register(
        String firstName,
        String lastName,
        String email,
        String password,
        String phone,
        String location,
        String experience,
        String skills,
        String resume // This should be a String (e.g., file URL or path)
    ) {
        logger.debug("Register called with email: {}, firstName: {}, lastName: {}, phone: {}, location: {}, experience: {}, skills: {}, resume: {}",
            email, firstName, lastName, phone, location, experience, skills, resume);

        userRepository.findByEmail(email).ifPresent(x -> {
            throw new CustomException("Email already in use", 400);
        });

        User u = new User();
        u.setName((firstName + " " + lastName).trim());
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setPhone(phone);
        u.setLocation(location);
        u.setExperience(experience);
        u.setSkills(skills);
        u.setResume(resume);
        u = userRepository.save(u);

        return Map.of("message", "User registered successfully", "user", toDto(u));
    }

    public Map<String, Object> login(String email, String password) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Invalid credentials", 401));

        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new CustomException("Invalid credentials", 401);
        }

        // TODO: integrate JwtUtil to generate a real token later
        return Map.of("token", "mock-jwt-token", "user", toDto(u));
    }

    public UserDTO getProfile(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", 404));
        return toDto(u);
    }

    public UserDTO updateProfile(Long userId, UserDTO update) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", 404));

        if (update.getName() != null) {
            u.setName(update.getName());
        }
        if (update.getEmail() != null) {
            u.setEmail(update.getEmail());
        }
        if (update.getRole() != null) {
            u.setRole(User.Role.valueOf(update.getRole().toUpperCase()));
        }

        u = userRepository.save(u);
        return toDto(u);
    }
}