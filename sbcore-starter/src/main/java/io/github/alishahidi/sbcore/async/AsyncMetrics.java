package io.github.alishahidi.sbcore.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncMetrics {

    private final AsyncThreadPoolTaskExecutor executor;

    public AsyncMetrics(AsyncThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    @Scheduled(fixedRate = 5000) // هر ۵ ثانیه یک بار وضعیت را بررسی کن
    public void logAsyncTaskStatus() {
        int activeCount = executor.getActiveCount();
        int poolSize = executor.getPoolSize();
        System.out.println("Async Thread Pool Status: Active Threads = " + activeCount + ", Total Pool Size = " + poolSize);
    }
}

