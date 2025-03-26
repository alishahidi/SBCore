package net.alishahidi.sbcore.helloworld;

import net.alishahidi.sbcore.core.entity.BaseMapper;
import net.alishahidi.sbcore.core.entity.BaseRepository;
import net.alishahidi.sbcore.core.entity.BaseService;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldCreateDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldLoadDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldUpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloWorldService extends BaseService<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {

    HelloWorldRepository repository;
    HelloWorldMapper mapper = Mappers.getMapper(HelloWorldMapper.class);

    @Override
    protected BaseRepository<HelloWorldEntity> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> getMapper() {
        return mapper;
    }
}
