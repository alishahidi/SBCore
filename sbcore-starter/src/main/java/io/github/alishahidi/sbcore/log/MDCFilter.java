package io.github.alishahidi.sbcore.log;

import io.github.alishahidi.sbcore.util.QuartzUtils;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            MDC.put("traceId", UUID.randomUUID().toString());
            MDC.put("ip", QuartzUtils.getClientIp());
            MDC.put("serverIp", QuartzUtils.getServerIp());
            MDC.put("username", QuartzUtils.getUsername());

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}