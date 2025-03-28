package io.github.alishahidi.sbcore.quartz.api;

import io.github.alishahidi.sbcore.quartz.entity.JobStatus;
import io.github.alishahidi.sbcore.quartz.service.JobExecutionService;
import io.github.alishahidi.sbcore.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobExecutionController {
    JobExecutionService jobExecutionService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<JobExecutionDto>>> loadAll() {
        return ResponseEntity.ok(jobExecutionService.getAllJobsForUser());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<JobExecutionDto>>> loadByStatus(@PathVariable JobStatus status) {
        return ResponseEntity.ok(jobExecutionService.getJobsByStatus(status));
    }
}
