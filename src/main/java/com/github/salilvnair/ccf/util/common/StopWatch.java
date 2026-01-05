package com.github.salilvnair.ccf.util.common;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    private long startTime;
    private long stopTime;
    private long durationInMillis;

    public StopWatch() {
    }

    public static StopWatch start() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.startTime = System.currentTimeMillis();
        return stopWatch;
    }

    public static long stop(StopWatch stopWatch) {
        stopWatch.stopTime = System.currentTimeMillis();
        stopWatch.durationInMillis = stopWatch.stopTime - stopWatch.startTime;
        return stopWatch.durationInMillis;
    }

    public static long elapsed(StopWatch stopWatch, TimeUnit timeUnit) {
        TimeUnit time = TimeUnit.MILLISECONDS;
        return timeUnit.convert(stop(stopWatch), time);
    }
}