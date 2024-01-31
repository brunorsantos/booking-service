package com.technical.util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.slf4j.MDC;

import java.util.UUID;

public class Logging {

    // Add and id to the MDC context to help with log debugging,
    // this way all logs will have the id field
    public static void addLoggingContextId() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String apiPath = request.getRequestURI();
        MDC.put("apiPath", apiPath);
    }

}
