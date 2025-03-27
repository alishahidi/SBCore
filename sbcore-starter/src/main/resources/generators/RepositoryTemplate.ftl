package ${basePackage}.${entityName?lower_case};

import io.github.alishahidi.sbcore.entity.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entityName}Repository extends BaseRepository<${entityName}Entity> {
}