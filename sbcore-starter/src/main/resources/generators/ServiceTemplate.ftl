package ${basePackage}.${entityName?lower_case};

import io.github.alishahidi.sbcore.entity.BaseMapper;
import io.github.alishahidi.sbcore.entity.BaseRepository;
import io.github.alishahidi.sbcore.entity.BaseService;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}CreateDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}LoadDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}UpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ${entityName}Service extends BaseService<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> {
    ${entityName}Repository repository;
    ${entityName}Mapper mapper = Mappers.getMapper(${entityName}Mapper.class);

    @Override
    protected BaseRepository<${entityName}Entity> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> getMapper() {
        return mapper;
    }

    <#if searchableFields?has_content>
    @Override
    protected List<String> getSearchableFields() {
        return Arrays.asList(
        <#list searchableFields as field>
            "${field}"<#sep>, </#sep>
        </#list>
        );
    }
    </#if>
}
