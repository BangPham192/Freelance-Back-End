package com.backend.freelance.controllers;

import com.backend.freelance.dtos.JobApplicationDto;
import com.backend.freelance.dtos.JobDto;
import com.backend.freelance.dtos.SkillDto;
import com.backend.freelance.http.PageRequestCustom;
import com.backend.freelance.interfaces.IJobController;
import com.backend.freelance.models.JobApplicationsStatus;
import com.backend.freelance.request.CreateJobRequest;
import com.backend.freelance.request.JobApplyRequest;
import com.backend.freelance.services.JobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
public class JobController extends BaseController implements IJobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public JobDto createJob(CreateJobRequest request) {
        UserDetails userDetails = getCurrentUser();
        return jobService.createJob(request, userDetails);
    }

    @Override
    public List<SkillDto> getSkills() {
        return jobService.getSkills();
    }

    @Override
    public Page<JobDto> getAllJobs(PageRequestCustom pageRequest) {
        return jobService.getAllJobs(pageRequest);
    }

    @Override
    public JobDto getJobById(UUID id) {
        return jobService.getJobById(id);
    }

    @Override
    public ResponseEntity<HttpStatus> applyForJob(UUID id, String jsonData, MultipartFile files) {
        String username = getCurrentUser().getUsername();
        ObjectMapper mapper = new ObjectMapper();
        JobApplyRequest request = null;
        try {
            request = mapper.readValue(jsonData, JobApplyRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jobService.applyForJob(id, request, files, username);
    }

    @Override
    public Page<JobApplicationDto> getMyJobApplicationsByStatuses(PageRequestCustom pageRequest) {
        String username = getCurrentUser().getUsername();
        return jobService.getMyJobApplicationsByStatuses(pageRequest, List.of(JobApplicationsStatus.APPLICATIONS_STATUSES), username);
    }

    @Override
    public Page<JobApplicationDto> getMyActiveJobApplications(PageRequestCustom pageRequest) {
        String username = getCurrentUser().getUsername();
        return jobService.getMyJobApplicationsByStatuses(pageRequest, List.of(JobApplicationsStatus.ACCEPTED), username);
    }
}
