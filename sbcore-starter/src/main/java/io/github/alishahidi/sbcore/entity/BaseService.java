package io.github.alishahidi.sbcore.entity;

import io.github.alishahidi.sbcore.search.SearchParamsWrapper;
import io.github.alishahidi.sbcore.util.SpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected abstract BaseRepository<T> getRepository();

    protected abstract BaseMapper<T, C, U, L> getMapper();

    @Transactional
    public L saveOrUpdate(Long id, U updateDto, C createDto) {
        log.info("saveOrUpdate called with id: {}", id);
        try {
            L result = (id != null) ? update(id, updateDto) : create(createDto);
            log.info("saveOrUpdate completed successfully for id: {}", id);
            return result;
        } catch (Exception e) {
            log.error("Error in saveOrUpdate for id: {}", id, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<L> search(MultiValueMap<String, String> params, Pageable pageable) {
        log.debug("search called with params: {}", params);
        Specification<T> spec = SpecificationBuilder.buildSpecification(params, getSearchableFields());

        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt")));

        Page<L> result = getRepository().findAll(spec, pageable).map(getMapper()::load);
        log.debug("search returned {} elements", result.getTotalElements());
        return result;
    }

    @Transactional(readOnly = true)
    public Page<L> search(SearchParamsWrapper params) {
        log.debug("search(SearchParamsWrapper) called");
        return search(params.filters, params.getPageable());
    }

    protected List<String> getSearchableFields() {
        return List.of("id");
    }

    public L update(Long id, U updateDto) {
        log.info("update called for id: {}", id);
        Optional<T> existingEntityOpt = getRepository().findById(id);

        if (existingEntityOpt.isPresent() && existingEntityOpt.get().getDeletedAt() == null) {
            T existingEntity = existingEntityOpt.get();
            getMapper().update(updateDto, existingEntity);
            existingEntity.setId(id);
            existingEntity.setUpdatedAt(new Date());
            beforeSaveOrUpdate(existingEntity);
            L result = getMapper().load(getRepository().save(existingEntity));
            log.info("update successful for id: {}", id);
            return result;
        } else {
            log.warn("update failed, entity not found or deleted for id: {}", id);
            throw new EntityNotFoundException("Entity with ID " + id + " not found or has been deleted.");
        }
    }

    public T pureUpdate(Long id, T entity) {
        log.info("pureUpdate called for id: {}", id);
        Optional<T> existingEntityOpt = getRepository().findById(id);

        if (existingEntityOpt.isPresent() && existingEntityOpt.get().getDeletedAt() == null) {
            T existingEntity = existingEntityOpt.get();
            getMapper().pureUpdate(entity, existingEntity);
            existingEntity.setId(id);
            existingEntity.setUpdatedAt(new Date());
            beforeSaveOrUpdate(existingEntity);
            T saved = getRepository().save(existingEntity);
            log.info("pureUpdate successful for id: {}", id);
            return saved;
        } else {
            log.warn("pureUpdate failed, entity not found or deleted for id: {}", id);
            throw new EntityNotFoundException("Entity with ID " + id + " not found or has been deleted.");
        }
    }

    public L create(C createDto) {
        log.info("create called");
        T entity = getMapper().create(createDto);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        beforeSaveOrUpdate(entity);
        L result = getMapper().load(getRepository().save(entity));
        log.info("create successful, entity created with id: {}", entity.getId());
        return result;
    }

    public T pureCreate(T entity) {
        log.info("pureCreate called");
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        beforeSaveOrUpdate(entity);
        T saved = getRepository().save(entity);
        log.info("pureCreate successful, entity created with id: {}", saved.getId());
        return saved;
    }

    public void beforeSaveOrUpdate(T entity) {
        // override if needed
    }

    @Transactional(readOnly = true)
    public L findById(Long id) {
        log.debug("findById called for id: {}", id);
        return getRepository()
                .findByIdAndDeletedAtIsNull(id)
                .map(getMapper()::load)
                .orElseThrow(() -> {
                    log.warn("findById failed, entity not found: {}", id);
                    return new EntityNotFoundException("Entity not found");
                });
    }

    @Transactional(readOnly = true)
    public List<L> findAll() {
        log.debug("findAll called");
        List<L> results = getRepository().findByDeletedAtIsNull().stream().map(getMapper()::load).toList();
        log.debug("findAll returned {} items", results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public Page<L> findAll(Pageable pageable) {
        log.debug("findAll(Pageable) called");
        Page<L> result = getRepository().findByDeletedAtIsNull(pageable).map(getMapper()::load);
        log.debug("findAll(Pageable) returned {} items", result.getTotalElements());
        return result;
    }

    @Transactional
    public void deleteLogical(Long id) {
        log.info("deleteLogical called for id: {}", id);
        T entity = getRepository().findById(id)
                .orElseThrow(() -> {
                    log.warn("deleteLogical failed, entity not found: {}", id);
                    return new EntityNotFoundException("Entity with ID " + id + " not found");
                });

        entity.setDeletedAt(new Date());
        getRepository().save(entity);
        log.info("deleteLogical successful for id: {}", id);
    }

}
