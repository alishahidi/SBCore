package io.github.alishahidi.sbcore.async;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sbcore.async")
public class AsyncProperties {

    private int corePoolSize = 5;    // مقدار پیش‌فرض
    private int maxPoolSize = 10;    // مقدار پیش‌فرض
    private int queueCapacity = 500; // مقدار پیش‌فرض
}
