# Async Package

## Overview
The `async` package provides utilities to work with asynchronous operations in a Spring-based application. It includes configuration and execution features to handle background tasks, such as executing asynchronous methods and scheduling tasks.

## Key Classes:

### `AsyncConfig`
This class configures the Spring `@Async` behavior and sets up thread pool management. It enables the execution of asynchronous tasks across the application.

#### Key Methods:
- **`enableAsync()`**: Initializes the asynchronous execution mechanism.
- **`taskExecutor()`**: Defines the executor for managing the tasks.

### `AsyncExecutor`
This class is used to execute tasks asynchronously in a non-blocking manner.

#### Key Methods:
- **`submitTask()`**: Submits a task to be executed asynchronously.
