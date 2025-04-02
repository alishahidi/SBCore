package io.github.alishahidi.sbcore.async;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AsyncDynamicConfig {

    private final AsyncThreadPoolTaskExecutor executor;

    public AsyncDynamicConfig(AsyncThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    public void updatePoolSize(int corePoolSize, int maxPoolSize) {
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        System.out.println("Updated Async Thread Pool: corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize);
    }
}
