package com.alishahidi.sbcore.quartz.api;

import com.alishahidi.sbcore.quartz.entity.JobStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobExecutionDto {
    Long id;
    String jobName;
    String jobGroup;
    JobStatus status;
    Date createdAt;
}
