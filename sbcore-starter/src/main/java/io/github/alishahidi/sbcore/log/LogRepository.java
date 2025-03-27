package io.github.alishahidi.sbcore.log;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM LogEntity le WHERE le.createdAt <= :expirationDate")
    void deleteLogsOlderThan(@Param("expirationDate") Date expirationDate);
}
