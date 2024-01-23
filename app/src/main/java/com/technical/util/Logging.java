package com.technical.util;

import org.slf4j.MDC;

import java.util.UUID;

public class Logging {

    // Add and id to the MDC context to help with log debugging,
    // this way all logs will have the id field
    public static void addLoggingContextId(final UUID id) {
        MDC.put("id", id.toString());
    }

}
