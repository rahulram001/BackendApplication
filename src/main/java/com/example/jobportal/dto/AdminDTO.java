package com.example.jobportal.dto;

public class AdminDTO {
    private Long id;
    private String adminId;
    private String email;
    private String password;

    // ...getters and setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
