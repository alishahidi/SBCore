package com.alishahidi.sbcore;

import com.alishahidi.sbcore.generator.CodeGenerator;
import com.alishahidi.sbcore.generator.GeneratorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
public class SBCoreApplication {
    @Value("${generator.enabled:false}")
    private boolean generatorEnabled;

    @Value("${generator.xmlFolderPath:generate-xmls}")
    private String xmlFolderPath;

    public static void main(String[] args) {
        SpringApplication.run(SBCoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup() {
        return args -> {
            if (generatorEnabled) {
                GeneratorConfig config = GeneratorConfig.builder()
                        .xmlFolderPath(xmlFolderPath)
                        .build();

                CodeGenerator.run(config);
            }
        };
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
