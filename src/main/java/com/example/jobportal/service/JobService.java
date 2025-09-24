package com.example.jobportal.service;

import com.example.jobportal.dto.JobDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    public JobDTO createJob(JobDTO dto) {
        dto.setId(1L);
        return dto;
    }

    public JobDTO updateJob(Long id, JobDTO dto) {
        dto.setId(id);
        return dto;
    }

    public List<JobDTO> listJobs() {
        return new ArrayList<>();
    }

    public void deleteJob(Long id) {
    }

    public List<JobDTO> searchJobs(String keyword) {
        return new ArrayList<>();
    }
}
