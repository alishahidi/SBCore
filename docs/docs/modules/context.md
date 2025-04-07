# Context Package

## Overview
The `context` package provides utilities to interact with the Spring `ApplicationContext` and `Environment`. It simplifies the process of accessing Spring Beans and environment properties from anywhere in the application.

## Key Classes:

### `SpringContext`
This class provides static methods to access beans and properties from the Spring context. It ensures that the application context is properly initialized and offers methods for retrieving beans by type or name.

#### Key Methods:
- **`getBean(Class<T> beanClass)`**: Retrieves a bean of the specified type from the context.
- **`getProperty(String propertyName)`**: Retrieves a property from the Spring Environment.

#### Usage:
```java
UserService userService = SpringContext.getBean(UserService.class);
String port = SpringContext.getProperty("server.port");
