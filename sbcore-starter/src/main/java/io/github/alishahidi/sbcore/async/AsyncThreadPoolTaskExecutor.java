package io.github.alishahidi.sbcore.async;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private final AsyncProperties asyncProperties;

    public AsyncThreadPoolTaskExecutor(AsyncProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    @PostConstruct
    public void init() {
        setCorePoolSize(asyncProperties.getCorePoolSize());
        setMaxPoolSize(asyncProperties.getMaxPoolSize());
        setQueueCapacity(asyncProperties.getQueueCapacity());
        setThreadNamePrefix("AsyncThread-");
        initialize();
    }

    // متد جدید برای تغییر تعداد Threadها در زمان اجرا
    public void updatePoolSize(int corePoolSize, int maxPoolSize) {
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maxPoolSize);
        System.out.println("Updated Async Thread Pool: corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize);
    }
}
