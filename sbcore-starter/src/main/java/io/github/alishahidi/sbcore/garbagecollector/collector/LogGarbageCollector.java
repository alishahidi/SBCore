package io.github.alishahidi.sbcore.garbagecollector.collector;

import io.github.alishahidi.sbcore.garbagecollector.GarbageCollector;
import io.github.alishahidi.sbcore.log.LogRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
@Getter
@Setter
public class LogGarbageCollector implements GarbageCollector {

    @Value("${garbage.collector.log.time}")
    Long interval;

    @Value("${garbage.collector.log.expire}")
    Long retentionPeriod;

    final LogRepository logRepository;

    public LogGarbageCollector(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void collect() {
        Date expirationDate = Date.valueOf(LocalDate.now().minusDays(retentionPeriod));

        logRepository.deleteLogsOlderThan(expirationDate);
        System.out.println("Old log entries removed up to " + expirationDate);
    }

    @Override
    public Long getTime() {
        return interval;
    }
}
