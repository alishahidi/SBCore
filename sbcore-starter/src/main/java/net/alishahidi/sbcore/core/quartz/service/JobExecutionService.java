package net.alishahidi.sbcore.core.quartz.service;

import net.alishahidi.sbcore.core.quartz.api.JobExecutionDto;
import net.alishahidi.sbcore.core.quartz.api.JobExecutionMapper;
import net.alishahidi.sbcore.core.quartz.entity.JobExecutionEntity;
import net.alishahidi.sbcore.core.quartz.entity.JobStatus;
import net.alishahidi.sbcore.core.quartz.repository.JobExecutionRepository;
import net.alishahidi.sbcore.core.response.ApiResponse;
import net.alishahidi.sbcore.core.util.AuthUtils;
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
