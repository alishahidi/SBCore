package com.alishahidi.sbcore.entity;

import com.alishahidi.sbcore.agent.dto.AgentLoadDto;
import com.alishahidi.sbcore.search.SearchParamsWrapper;
import com.alishahidi.sbcore.util.SpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    protected abstract BaseRepository<T> getRepository();

    protected abstract BaseMapper<T, C, U, L> getMapper();

    @Transactional
    public L saveOrUpdate(Long id, U updateDto, C createDto) {
        if (id != null) {
            return update(id, updateDto);
        } else {
            return create(createDto);
        }
    }

    @Transactional(readOnly = true)
    public Page<L> search(MultiValueMap<String, String> params, Pageable pageable) {
        Specification<T> spec = SpecificationBuilder.buildSpecification(params, getSearchableFields());
        return getRepository().findAll(spec, pageable).map(getMapper()::load);
    }

    @Transactional(readOnly = true)
    public Page<L> search(SearchParamsWrapper params) {
        return search(params.filters, params.getPageable());
    }

    protected List<String> getSearchableFields(){
        return List.of("id");
    };

    public L update(Long id, U updateDto) {
        Optional<T> existingEntityOpt = getRepository().findById(id);

        if (existingEntityOpt.isPresent() && existingEntityOpt.get().getDeletedAt() == null) {
            T existingEntity = existingEntityOpt.get();
            getMapper().update(updateDto, existingEntity);
            existingEntity.setId(id);
            existingEntity.setUpdatedAt(new Date());
            beforeSaveOrUpdate(existingEntity);
            return getMapper().load(getRepository().save(existingEntity));
        } else {
            throw new EntityNotFoundException("Entity with ID " + id + " not found or has been deleted.");
        }
    }

    public T pureUpdate(Long id, T entity) {
        Optional<T> existingEntityOpt = getRepository().findById(id);

        if (existingEntityOpt.isPresent() && existingEntityOpt.get().getDeletedAt() == null) {
            T existingEntity = existingEntityOpt.get();
            getMapper().pureUpdate(entity, existingEntity);
            existingEntity.setId(id);
            existingEntity.setUpdatedAt(new Date());
            beforeSaveOrUpdate(existingEntity);
            return getRepository().save(existingEntity);
        } else {
            throw new EntityNotFoundException("Entity with ID " + id + " not found or has been deleted.");
        }
    }

    public L create(C createDto) {
        T entity = getMapper().create(createDto);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        beforeSaveOrUpdate(entity);
        return getMapper().load(getRepository().save(entity));
    }

    public T pureCreate(T entity) {
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        beforeSaveOrUpdate(entity);
        return getRepository().save(entity);
    }

    public void beforeSaveOrUpdate(T entity) {
    }

    @Transactional(readOnly = true)
    public L findById(Long id) {
        return getRepository().findByIdAndDeletedAtIsNull(id).map(getMapper()::load).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Transactional(readOnly = true)
    public List<L> findAll() {
        return getRepository().findByDeletedAtIsNull().stream().map(getMapper()::load).toList();
    }

    @Transactional(readOnly = true)
    public Page<L> findAll(Pageable pageable) {
        return getRepository()
                .findByDeletedAtIsNull(pageable)
                .map(getMapper()::load);
    }

    @Transactional
    public void deleteLogical(Long id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));

        entity.setDeletedAt(new Date());

        getRepository().save(entity);
    }

}
