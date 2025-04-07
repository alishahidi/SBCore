package io.github.alishahidi.sbcore.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        System.err.println("Exception in Async Method: " + method.getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
    }
}