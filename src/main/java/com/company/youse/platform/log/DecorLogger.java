package com.company.youse.platform.log;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DecorLogger {
    private static final String FINISHED = "Finished";

    private static final String STARTED = "Started";

    public static void logServiceStart(String name) {
        log.trace( name, "Service ",  STARTED);
    }

    public static void logServiceFinish(String name) {
        log.trace( name, "Service ", FINISHED);
    }

    public static void logDecorStart(String name, String decor) {
        log.trace(name, decor, STARTED);
    }

    public static void logDecorFinish(String name, String decor) {
        log.trace(name, decor, FINISHED);
    }

    public static void logBusStart(String name) {
        log.trace(name, "Bus", STARTED);
    }

    public static void logBusFinish(String name) {
        log.trace(name, "Bus", FINISHED);
    }
}
