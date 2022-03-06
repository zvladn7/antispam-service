package ru.spbstu.kafka;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JobManager {

    private static final Logger log = LoggerFactory.getLogger(JobManager.class);

    private final Runnable runnable;
    private final ScheduledExecutorService executorService;
    private final Long period;
    private final long initialDelay;
    private ScheduledFuture<?> future;

    public JobManager(@NotNull Runnable runnable,
                      @NotNull ScheduledExecutorService executorService,
                      @NotNull Long period,
                      long initialDelay) {
        Validate.notNull(runnable);
        Validate.notNull(executorService);

        this.runnable = runnable;
        this.executorService = executorService;
        this.period = period;
        this.initialDelay = initialDelay;
    }

    public synchronized boolean cancel() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
            return future.isDone();
        }
        return true;
    }

    public synchronized void scheduleIfNotRunning() {
        if (future == null || future.isDone()) {
            if (future != null && !future.isCancelled()) {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error("Error in component [{}]", runnable, e);
                }
            }
            schedule();
        }
    }

    private void schedule() {
        if (future != null && !future.isDone()) {
            log.error("the job is already running, no need to schedule second processing thread");
            return;
        }
        if (period != null) {
            log.info("scheduling [{}], initial delay [{}] seconds, period [{}] seconds", runnable, initialDelay, period);
            future = executorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS);
        } else {
            log.info("scheduling [{}] for single run, initial delay [{}] seconds", runnable, initialDelay);
            future = executorService.schedule(runnable, initialDelay, TimeUnit.SECONDS);
        }
    }

}
