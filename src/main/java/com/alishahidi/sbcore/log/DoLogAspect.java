package com.alishahidi.sbcore.log;

import com.alishahidi.sbcore.util.LogUtils;
import com.alishahidi.sbcore.util.QuartzUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class DoLogAspect {
    HttpServletRequest request;
    LogRepository logRepository;
    ObjectMapper objectMapper;

    @Around("@annotation(doLog)")
    @Transactional
    public Object logExecution(ProceedingJoinPoint joinPoint, DoLog doLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        LogEntity logEntity = new LogEntity();
        logEntity.setTraceId(UUID.randomUUID().toString());
        logEntity.setMethod(joinPoint.getSignature().toShortString());

        String endpoint = request.getMethod() + " " + request.getRequestURI();
        logEntity.setEndpoint(endpoint);

        logEntity.setIp(QuartzUtils.getClientIp());
        logEntity.setServerIp(QuartzUtils.getServerIp());
        logEntity.setUsername(QuartzUtils.getUsername());

        if (doLog.logRequest()) {
            logEntity.setRequestBody(LogUtils.filterObject(joinPoint.getArgs(), objectMapper));
            logEntity.setRequestPayloadSize((long) logEntity.getRequestBody().getBytes(StandardCharsets.UTF_8).length);
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
            logEntity.setStatus(LogStatus.SUCCESS);
        } catch (Exception e) {
            logEntity.setStatus(LogStatus.FAILED);
            logEntity.setErrorMessage(e.getMessage());
            logEntity.setStackTrace(Arrays.toString(e.getStackTrace()));
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logEntity.setProcessTime(duration);
            if (doLog.logResponse()) {
                logEntity.setResponseBody(LogUtils.filterObject(result, objectMapper));
                logEntity.setResponsePayloadSize((long) logEntity.getResponseBody().getBytes(StandardCharsets.UTF_8).length);
            }
            logRepository.save(logEntity);
        }

        return result;
    }
}
