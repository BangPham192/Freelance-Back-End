package com.backend.freelance.interfaces;

import com.backend.freelance.dtos.JobApplicationDto;
import com.backend.freelance.dtos.JobDto;
import com.backend.freelance.dtos.SkillDto;
import com.backend.freelance.http.page.PageRequestCustom;
import com.backend.freelance.request.CreateJobRequest;
import com.backend.freelance.request.JobApplyRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job")
public interface IJobController {

    @PostMapping
    JobDto createJob(@RequestBody CreateJobRequest request);

    @GetMapping("/skills")
    List<SkillDto> getSkills();

    @GetMapping("/getAll")
    Page<JobDto> getAllJobs(PageRequestCustom pageRequest);

    @GetMapping("/{id}")
    JobDto getJobById(@PathVariable("id") UUID id);

    @PostMapping(value = "/{id}/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<HttpStatus> applyForJob(@PathVariable("id") UUID id,
                                           @RequestPart(value = "data", required = false) String jsonData,
                                           @RequestPart(value = "files", required = false) MultipartFile files);
    @GetMapping("/applications")
    Page<JobApplicationDto> getMyJobApplications(PageRequestCustom pageRequest);
}
