package io.github.alishahidi.sbcore.quartz.api;

import io.github.alishahidi.sbcore.quartz.entity.JobExecutionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobExecutionMapper {
    JobExecutionMapper INSTANCE = Mappers.getMapper(JobExecutionMapper.class);

    JobExecutionDto toDTO(JobExecutionEntity jobExecutionEntity);
    List<JobExecutionDto> toDTOList(List<JobExecutionEntity> jobExecutionEntities);
}