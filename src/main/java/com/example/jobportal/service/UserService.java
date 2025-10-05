package com.example.jobportal.service;

import com.example.jobportal.dto.UserDTO;
import com.example.jobportal.exception.CustomException;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setLocation(user.getLocation());
        dto.setExperience(user.getExperience());
        dto.setSkills(user.getSkills());
        dto.setResume(user.getResume());
        dto.setStatus(user.getStatus());
        dto.setSummary(user.getSummary());
        dto.setEducation(user.getEducation());
        dto.setJoinedDate(user.getJoinedDate());
        dto.setRole(user.getRole());
        dto.setLastActive(user.getLastActive()); // <-- Map lastActive if present
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
        String resume
    ) {
        logger.debug("Register called with email: {}, firstName: {}, lastName: {}, phone: {}, location: {}, experience: {}, skills: {}, resume: {}",
            email, firstName, lastName, phone, location, experience, skills, resume);

        userRepository.findByEmail(email).ifPresent(x -> {
            throw new CustomException("Email already in use", 400);
        });

        // Defensive: check for null or empty for all required fields
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            location == null || location.trim().isEmpty() ||
            experience == null || experience.trim().isEmpty() ||
            skills == null || skills.trim().isEmpty()) {
            throw new CustomException("All fields except resume are required and must not be blank.", 400);
        }

        User u = new User();
        u.setName((firstName + " " + lastName).trim());
        u.setEmail(email.trim());
        u.setPassword(passwordEncoder.encode(password));
        u.setPhone(phone.trim());
        u.setLocation(location.trim());
        u.setExperience(experience.trim());
        u.setSkills(skills.trim());
        u.setResume(resume);
        u.setStatus("ACTIVE");

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


        u = userRepository.save(u);
        return toDto(u);
    }

    // MISSING: updateUserStatus (for admin to activate/deactivate/suspend users)
    public UserDTO updateUserStatus(Long userId, String status) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", 404));
        u.setStatus(status);
        u = userRepository.save(u);
        return toDto(u);
    }

    // MISSING: updateUserRole (for admin to change user role)
    // REMOVE this method if you have removed role from User entity
    // public UserDTO updateUserRole(Long userId, String role) {
    //     User u = userRepository.findById(userId)
    //             .orElseThrow(() -> new CustomException("User not found", 404));
    //     u.setRole(User.Role.valueOf(role.toUpperCase()));
    //     u = userRepository.save(u);
    //     return toDto(u);
    // }

    // MISSING: deleteUser (for admin to delete a user)
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found", 404);
        }
        userRepository.deleteById(userId);
    }

    // MISSING: bulkUpdateUsers (for admin bulk actions)
    public void bulkUpdateUsers(java.util.List<Long> userIds, String action) {
        for (Long userId : userIds) {
            if ("delete".equalsIgnoreCase(action)) {
                deleteUser(userId);
            } else if ("activate".equalsIgnoreCase(action)) {
                updateUserStatus(userId, "ACTIVE");
            } else if ("deactivate".equalsIgnoreCase(action)) {
                updateUserStatus(userId, "INACTIVE");
            }
            // Add more actions as needed
        }
    }
}