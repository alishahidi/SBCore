package io.github.alishahidi.sbcore.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides utility methods to interact with Spring's ApplicationContext and Environment.
 * It allows easy access to Spring Beans and properties throughout the application.
 *
 * <p>SpringContext is a singleton class that holds the ApplicationContext and provides
 * methods to fetch Beans by type, retrieve property values from the environment,
 * and ensure that the ApplicationContext is correctly initialized.</p>
 *
 * <p>Usage examples:</p>
 * <pre>
 *     UserService userService = SpringContext.getBean(UserService.class);
 *     String port = SpringContext.getProperty("server.port");
 * </pre>
 *
 * <p>This class is automatically initialized by Spring through the ApplicationContextAware interface.</p>
 */
@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * This method is called by Spring to set the ApplicationContext.
     *
     * @param applicationContext the Spring ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }

    /**
     * Fetches a Spring Bean from the ApplicationContext by type.
     *
     * @param beanClass the class type of the bean
     * @param <T> the type of the bean
     * @return the Spring Bean of the specified type
     * @throws IllegalStateException if the ApplicationContext has not been initialized
     */
    public static <T> T getBean(Class<T> beanClass) {
        ensureContextIsSet();
        return context.getBean(beanClass);
    }

    /**
     * Fetches all Spring Beans of the specified type from the ApplicationContext.
     *
     * @param beanClass the class type of the beans
     * @param <T> the type of the beans
     * @return a list of Beans of the specified type
     * @throws IllegalStateException if the ApplicationContext has not been initialized
     */
    public static <T> List<T> getBeans(Class<T> beanClass) {
        ensureContextIsSet();
        return new ArrayList<>(context.getBeansOfType(beanClass).values());
    }

    /**
     * Fetches a property value from the Spring Environment.
     *
     * @param propertyName the name of the property
     * @return the value of the property
     * @throws IllegalStateException if the ApplicationContext has not been initialized
     */
    public static String getProperty(String propertyName) {
        ensureContextIsSet();
        Environment environment = context.getBean(Environment.class);
        return environment.getProperty(propertyName);
    }

    /**
     * Ensures that the ApplicationContext has been initialized before use.
     *
     * @throws IllegalStateException if the ApplicationContext is not set
     */
    private static void ensureContextIsSet() {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized yet.");
        }
    }
}
