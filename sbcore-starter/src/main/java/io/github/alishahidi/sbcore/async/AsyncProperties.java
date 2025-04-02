package io.github.alishahidi.sbcore.async;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sbcore.async")
public class AsyncProperties {

    private int corePoolSize = 5;    // مقدار پیش‌فرض
    private int maxPoolSize = 10;    // مقدار پیش‌فرض
    private int queueCapacity = 500; // مقدار پیش‌فرض

    // Getter و Setter برای هر فیلد
    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
