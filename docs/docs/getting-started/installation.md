---
sidebar_position: 1
---

# Installation

## Prerequisites

Ensure the following are installed:

- **Java 21** (or a newer version)
- **Maven** (for managing dependencies and building the project)
- **PostgreSQL** (or another database if required)
- 
## Create the Project

Ensure that you've created a Spring Boot project. You can generate a basic Spring Boot project using Spring Initializr.

## Add SBCore Dependencies
Since your project uses SBCore for job handling and code generation, we need to add the dependencies from the SBCore library to your `pom.xml`.

## Define the Parent POM

```xml
<parent>
    <groupId>io.github.alishahidi.sbcore</groupId>
    <artifactId>sbcore-parent</artifactId>
    <version>{version}</version>
</parent>
```

## Add SBCore Dependencies

We need to add three main dependencies:

### sbcore-starter
The main library for job handling
```xml
<dependency>
    <groupId>io.github.alishahidi.sbcore</groupId>
    <artifactId>sbcore-starter</artifactId>
    <version>{version}</version>
</dependency>
```

### sbcore-starter-provided
For additional provided functionalities.
```xml
<dependency>
    <groupId>io.github.alishahidi.sbcore</groupId>
    <artifactId>sbcore-starter-provided</artifactId>
    <version>{version}</version>
</dependency>
```

### sbcore-starter-test
For testing purposes.
```xml
<dependency>
    <groupId>io.github.alishahidi.sbcore</groupId>
    <artifactId>sbcore-starter-test</artifactId>
    <version>{version}</version>
</dependency>
```

## Add Required Dependencies

### Lombok
For reducing boilerplate code.
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>{version}</version>
    <optional>true</optional>
</dependency>
```

### MapStruct
For object-to-object mappings.
```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>{version}</version>
</dependency>
```

### Database Driver
For database interactions.  (Or anything you want)
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Spring Boot Starter Test and Spring Security Test
For testing.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Configure Build Plugins

- **Spring Boot Maven Plugin**: To run and package the Spring Boot application.

- **Maven Compiler Plugin**: For setting the Java version and annotation processing.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <skip>false</skip>
                <mainClass>{your.main.class}</mainClass>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>{version}</version>
            <configuration>
                <source>{java.version}</source>
                <target>{java.version}</target>
                <parameters>true</parameters>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>{version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>{version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>{version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Full Example `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>io.github.alishahidi.sbcore</groupId>
        <artifactId>sbcore-parent</artifactId>
        <version>1.4.0</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>example</artifactId>
    <version>v1.0.0</version>
    <name>example</name>
    <description>example</description>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>io.github.alishahidi.sbcore</groupId>
            <artifactId>sbcore-starter</artifactId>
            <version>1.4.0</version>
        </dependency>
        <dependency>
            <groupId>io.github.alishahidi.sbcore</groupId>
            <artifactId>sbcore-starter-provided</artifactId>
            <version>1.4.0</version>
        </dependency>
        <dependency>
            <groupId>io.github.alishahidi.sbcore</groupId>
            <artifactId>sbcore-starter-test</artifactId>
            <version>1.4.0</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>1.5.0.Final</version>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <skip>false</skip>
                    <mainClass>com.hami.contract.ContractApplication</mainClass>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <parameters>true</parameters>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>1.5.0.Final</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>1.18.24</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>1.18.24</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
