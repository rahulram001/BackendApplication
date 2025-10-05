package com.example.jobportal.service;

import com.example.jobportal.dto.EmployeeDTO;
import com.example.jobportal.model.Employee;
import com.example.jobportal.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, Object> register(EmployeeDTO dto) {
        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        Employee emp = new Employee();
        emp.setEmployeeId(dto.getEmployeeId());
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setPassword(dto.getPassword());
        emp.setCompany(dto.getCompany());
        emp.setAddress(dto.getAddress());
        emp.setCity(dto.getCity());
        emp.setLocation(dto.getLocation());
        emp = employeeRepository.save(emp);

        Map<String, Object> result = new HashMap<>();
        result.put("user", toDto(emp));
        result.put("role", "employer");
        return result;
    }

    public Map<String, Object> login(String email, String password) {
        Optional<Employee> empOpt = employeeRepository.findByEmail(email);
        if (empOpt.isEmpty() || !empOpt.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("user", toDto(empOpt.get()));
        result.put("role", "employer");
        return result;
    }

    public EmployeeDTO toDto(Employee emp) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(emp.getId());
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setName(emp.getName());
        dto.setEmail(emp.getEmail());
        dto.setPassword(emp.getPassword());
        dto.setCompany(emp.getCompany());
        dto.setAddress(emp.getAddress());
        dto.setCity(emp.getCity());
        dto.setLocation(emp.getLocation());
        return dto;
    }
}
