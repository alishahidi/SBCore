package io.github.alishahidi.sbcore;

import io.github.alishahidi.sbcore.generator.CodeGenerator;
import io.github.alishahidi.sbcore.generator.GeneratorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
public class SBCoreApplication {

    @Value("${generator.xmlFolderPath:generate-xmls}")
    private String xmlFolderPath;

    public static void main(String[] args) {
        SpringApplication.run(SBCoreApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(name = "generator.enabled", havingValue = "true")
    public CommandLineRunner startup() {
        return args -> {
            GeneratorConfig config = GeneratorConfig.builder()
                    .xmlFolderPath(xmlFolderPath)
                    .build();

            CodeGenerator.run(config);
        };
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
