package com.alishahidi.sbcore.quartz.service;

import com.alishahidi.sbcore.quartz.api.JobExecutionDto;
import com.alishahidi.sbcore.quartz.api.JobExecutionMapper;
import com.alishahidi.sbcore.quartz.entity.JobExecutionEntity;
import com.alishahidi.sbcore.quartz.entity.JobStatus;
import com.alishahidi.sbcore.quartz.repository.JobExecutionRepository;
import com.alishahidi.sbcore.response.ApiResponse;
import com.alishahidi.sbcore.util.AuthUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobExecutionService {

    JobExecutionRepository jobExecutionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobExecutionEntity createJobEntry(String jobName, String jobGroup, String ip, String serverIp, String username, String jobParameters) {
        JobExecutionEntity jobExecution = JobExecutionEntity.builder()
                .jobName(jobName)
                .jobGroup(jobGroup)
                .status(JobStatus.PENDING)
                .ip(ip)
                .serverIp(serverIp)
                .username(username)
                .jobParameters(jobParameters)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return jobExecutionRepository.save(jobExecution);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateJobStatus(Long jobId, JobStatus status, String errorMessage, String stackTrace) {
        JobExecutionEntity jobExecution = jobExecutionRepository.findById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job execution not found"));

        jobExecution.setStatus(status);
        jobExecution.setCreatedAt(new Date());

        if (status == JobStatus.FAILED) {
            jobExecution.setErrorMessage(errorMessage);
            jobExecution.setStackTrace(stackTrace);
        }

        jobExecutionRepository.save(jobExecution);
    }


    public ApiResponse<List<JobExecutionDto>> getJobsByStatus(JobStatus status) {
        String username = AuthUtils.getAuthenticatedUsername();
        return ApiResponse.success(JobExecutionMapper.INSTANCE.toDTOList(jobExecutionRepository.findByStatusAndUsername(status, username)));
    }

    public ApiResponse<List<JobExecutionDto>> getAllJobsForUser() {

        String username = AuthUtils.getAuthenticatedUsername();
        return ApiResponse.success(JobExecutionMapper.INSTANCE.toDTOList(jobExecutionRepository.findByUsername(username)));
    }
}
