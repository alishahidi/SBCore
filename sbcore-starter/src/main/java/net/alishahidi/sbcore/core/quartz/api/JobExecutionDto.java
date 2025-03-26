package net.alishahidi.sbcore.core.quartz.api;

import net.alishahidi.sbcore.core.quartz.entity.JobStatus;
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
