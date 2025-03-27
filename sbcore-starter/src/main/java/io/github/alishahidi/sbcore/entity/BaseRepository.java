package io.github.alishahidi.sbcore.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    List<T> findByDeletedAtIsNull();
    Optional<T> findByIdAndDeletedAtIsNull(Long id);
    Page<T> findByDeletedAtIsNull(Pageable pageable);
}