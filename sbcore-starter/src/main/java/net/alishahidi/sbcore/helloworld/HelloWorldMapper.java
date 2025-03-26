package net.alishahidi.sbcore.helloworld;

import net.alishahidi.sbcore.core.entity.BaseMapper;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldCreateDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldLoadDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HelloWorldMapper extends BaseMapper<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {
}
