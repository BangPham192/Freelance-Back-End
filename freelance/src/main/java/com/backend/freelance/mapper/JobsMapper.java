package com.backend.freelance.mapper;

import com.backend.freelance.dtos.JobApplicationDto;
import com.backend.freelance.dtos.JobDto;
import com.backend.freelance.dtos.SkillDto;
import com.backend.freelance.models.JobApplications;
import com.backend.freelance.models.Jobs;
import com.backend.freelance.models.Skills;
import com.backend.freelance.request.CreateJobRequest;
import com.backend.freelance.request.JobApplyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;


@Mapper(imports = java.util.UUID.class)
public interface JobsMapper {
    JobsMapper INSTANCE = Mappers.getMapper(JobsMapper.class);

    @Mapping(target = "publicId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "budgetMax", source = "request.budgetMax", qualifiedByName = "mapDecimal")
    @Mapping(target = "budgetMin", source = "request.budgetMin", qualifiedByName = "mapDecimal")
    Jobs createJob(CreateJobRequest request);

    @Mapping(target = "id", source = "publicId")
    @Mapping(target = "clientName", source = "user.username")
    JobDto toDto(Jobs job);

    SkillDto mapToDto(Skills skill);

    List<SkillDto> mapToList(List<Skills> skills);

    @Mapping(target = "publicId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "expectedFee", source = "expectedFee", qualifiedByName = "mapDecimal")
    @Mapping(target = "estimatedTime", expression = "java(Integer.parseInt(request.getEstimatedTime()))")
    JobApplications mapFromRequest(JobApplyRequest request);

    @Mapping(target = "applicationsStatus", source = "status")
    @Mapping(target = "job", ignore = true)
    JobApplicationDto mapApplications(JobApplications jobApplications);

    @Named("mapDecimal")
    default BigDecimal mapDecimal(String budget) {
        return budget != null ? new BigDecimal(budget) : BigDecimal.ZERO;
    }
}
