package me.reb4ck.smp.database.types;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public abstract class SQL {
    protected final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
    protected final int MAXIMUM_POOL_SIZE = (Runtime.getRuntime().availableProcessors() * 2) + 1;
    protected final int MINIMUM_IDLE = Math.min(MAXIMUM_POOL_SIZE, 10);

    protected final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30);
    protected final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(10);
    protected final long LEAK_DETECTION_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
}