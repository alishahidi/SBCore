package io.github.alishahidi.sbcore.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpringContext {
    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        SpringContext.context = context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static <T> List<T> getBeans(Class<T> beanClass) {
        return context.getBeansOfType(beanClass).values().stream()
                .map(bean -> (T) bean)
                .collect(Collectors.toList());
    }

    public static String getProperty(String propertyName) {
        Environment environment = context.getBean(Environment.class);

        return environment.getProperty(propertyName);
    }
}
