package com.backend.freelance.services;

import com.backend.freelance.dtos.JobApplicationDto;
import com.backend.freelance.dtos.JobApplyEvent;
import com.backend.freelance.dtos.JobDto;
import com.backend.freelance.dtos.SkillDto;
import com.backend.freelance.http.PageRequestCustom;
import com.backend.freelance.mapper.JobsMapper;
import com.backend.freelance.models.*;
import com.backend.freelance.repository.*;
import com.backend.freelance.request.CreateJobRequest;
import com.backend.freelance.request.JobApplyRequest;
import com.backend.freelance.sender.JobApplyProducer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {
    private final JobsRepository jobsRepository;
    private final UserRepository userRepository;
    private final SkillsRepository skillsRepository;
    private final JobSkillsRepository jobSkillsRepository;
    private final JobApplicationsRepository jobApplicationRepository;
    private final FileStorageService fileService;
    private final JobApplyProducer jobApplyProducer;

    @Autowired
    public JobService(JobsRepository jobsRepository, UserRepository userRepository,
                      SkillsRepository skillsRepository,
                      JobSkillsRepository jobSkillsRepository,
                      JobApplicationsRepository jobApplicationRepository,
                      FileStorageService fileService,
                      JobApplyProducer jobApplyProducer) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
        this.skillsRepository = skillsRepository;
        this.jobSkillsRepository = jobSkillsRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.fileService = fileService;
        this.jobApplyProducer = jobApplyProducer;
    }

    @Transactional
    public JobDto createJob(CreateJobRequest request, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());

        // save job
        Jobs job = JobsMapper.INSTANCE.createJob(request);
        job.setUser(user);
        job.setStatus(JobStatus.IN_PROGRESS);
        jobsRepository.save(job);

        // save job skills
        List<Skills> skills = skillsRepository.findAllByPublicIdIn(request.getSkills());
        List<JobSkills> jobSkills = new ArrayList<>();
        for (Skills skill : skills) {
            JobSkills jobSkill = new JobSkills();
            jobSkill.setJob(job);
            jobSkill.setSkill(skill);
            jobSkills.add(jobSkill);
        }
        jobSkillsRepository.saveAll(jobSkills);


        return JobsMapper.INSTANCE.toDto(job);
    }

    public List<SkillDto> getSkills() {
        return JobsMapper.INSTANCE.mapToList(skillsRepository.findAll());
    }

    public Page<JobDto> getAllJobs(PageRequestCustom pageRequest) {
        PageRequest page = PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Jobs> jobsPage = jobsRepository.findAll(page);
        if (jobsPage.getTotalElements() == 0) {
            return new PageImpl<>(new ArrayList<>());
        }
        List<JobDto> jobDtos = jobsPage.getContent().stream()
            .map(job -> {
                JobDto dto = JobsMapper.INSTANCE.toDto(job);
                List<String> skillIds = jobSkillsRepository.findAllByJobPublicId(job.getPublicId()).stream()
                    .map(js -> js.getSkill() != null ? js.getSkill().getName() : null)
                    .filter(java.util.Objects::nonNull)
                    .toList();
                dto.setSkills(skillIds);
                return dto;
            })
            .toList();
        return new PageImpl<>(jobDtos, page, jobsPage.getTotalElements());
    }

    public JobDto getJobById(UUID id) {
        Jobs job = jobsRepository.findByPublicId(id);
        if (job == null) {
            throw new RuntimeException("Job not found");
        }
        JobDto jobDto = JobsMapper.INSTANCE.toDto(job);
        List<String> skillIds = jobSkillsRepository.findAllByJobPublicId(job.getPublicId()).stream()
                .map(js -> js.getSkill() != null ? js.getSkill().getName() : null)
                .filter(java.util.Objects::nonNull)
                .toList();
        jobDto.setSkills(skillIds);

        // Fetch job applications if needed
        List<JobApplicationDto> applications = jobApplicationRepository.findAllByJobIdAndStatusIn(job.getId(),
                List.of(JobApplicationsStatus.PENDING)).stream()
                .map(JobsMapper.INSTANCE::mapApplications)
                .toList();
        jobDto.setJobApplications(applications);
        return jobDto;
    }

    public ResponseEntity<HttpStatus> applyForJob(UUID id, JobApplyRequest request, MultipartFile file, String username) {
        Jobs job = jobsRepository.findByPublicId(id);
        if (job == null) {
            throw new RuntimeException("Job not found");
        }

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Here you would handle the file uploads and save them accordingly
        // For simplicity, we are not implementing file handling in this example
        List<String> fileUrls = new ArrayList<>();
        if (file != null) {
//            for (MultipartFile file : files) {
                // Save the file and get the URL (this part is not implemented here)
                 try {
                     String fileUrl = fileService.saveFile(file);
                      fileUrls.add(fileUrl);
                 } catch (IOException e) {
                     throw new RuntimeException("Failed to save file: " + e.getMessage());
                 }
//            }
        }


        // Create a new JobApplication
        JobApplications application = JobsMapper.INSTANCE.mapFromRequest(request);
        application.setJob(job);
        application.setFreelancer(user);
        application.setStatus(JobApplicationsStatus.PENDING);
        application.setAttachmentUrl(fileUrls.getFirst()); // Assuming you want to save the first file URL
        application.setAppliedAt(LocalDateTime.now());

        // Save the application (assuming you have a JobApplicationRepository)
         jobApplicationRepository.save(application);

        JobApplyEvent event = JobApplyEvent.builder()
            .jobName(job.getTitle())
            .freelancerEmail(user.getEmail())
            .clientEmail(job.getUser().getEmail())
            .body(application.getCoverLetter())
            .attachmentUrl(fileUrls.isEmpty() ? null : fileUrls.getFirst())
            .build();

        // Produce a Kafka event
        jobApplyProducer.sendJobAppliedEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpStatus.CREATED);
    }

    public Page<JobApplicationDto> getMyJobApplicationsByStatuses(PageRequestCustom pageRequest, List<JobApplicationsStatus> statuses, String username) {
        PageRequest page = PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "appliedAt"));
        User user = userRepository.findByEmail(username);
        Page<JobApplications> applications = jobApplicationRepository.findAllByFreelancerIdAndStatusIn(user.getId(), statuses, page);
        if (applications.isEmpty()) {
            return new PageImpl<>(new LinkedList<>(), page, 0);
        }
        List<JobApplicationDto> applicationDtos = applications.getContent().stream()
                .map(app -> {
                    JobApplicationDto dto = JobsMapper.INSTANCE.mapApplications(app);
                    dto.setJob(JobsMapper.INSTANCE.toDto(app.getJob()));
                    return dto;
                })
                .toList();
        return new PageImpl<>(applicationDtos, page, applications.getTotalElements());
    }
}
