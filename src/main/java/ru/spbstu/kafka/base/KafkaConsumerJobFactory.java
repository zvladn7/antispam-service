// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class KafkaConsumerJobFactory<T> {

    private static final int POOL_SIZE = 2;

    private final String componentId;
    private final Properties consumerConfiguration;
    private final KafkaJobConfiguration jobConfiguration;
    private final MessageProcessor<T> messageProcessor;
    private final MessageParser<T> messageParser;

    private ScheduledExecutorService executor;

    public KafkaConsumerJobFactory(@NotNull String componentId,
                                   @NotNull Properties consumerConfiguration,
                                   @NotNull KafkaJobConfiguration jobConfiguration,
                                   @NotNull MessageProcessor<T> messageProcessor,
                                   @NotNull MessageParser<T> messageParser) {
        Validate.notNull(componentId);
        Validate.notNull(consumerConfiguration);
        Validate.notNull(jobConfiguration);
        Validate.notNull(messageParser);
        Validate.notNull(messageParser);

        this.componentId = componentId;
        this.consumerConfiguration = consumerConfiguration;
        this.jobConfiguration = jobConfiguration;
        this.messageProcessor = messageProcessor;
        this.messageParser = messageParser;
    }

    public void init() {
        executor = Executors.newScheduledThreadPool(POOL_SIZE);
        KafkaConsumerJob<T> consumerJob = new KafkaConsumerJob<>(
                componentId, jobConfiguration, consumerConfiguration, messageProcessor, messageParser);
        JobManager consumerJobManager = new JobManager(consumerJob, executor, null, jobConfiguration.getInitialDelayInSeconds());
        JobManager healthCheckerJobManager = new JobManager(healthChecker(consumerJobManager),
                executor, jobConfiguration.getConsumerHealthCheckPeriodInSeconds(), jobConfiguration.getInitialDelayInSeconds());
        consumerJobManager.scheduleIfNotRunning();
        healthCheckerJobManager.scheduleIfNotRunning();
    }

    @NotNull
    protected Runnable healthChecker(@NotNull JobManager manager) {
        Validate.notNull(manager);
        return new Runnable() {
            @Override
            public void run() {
                manager.scheduleIfNotRunning();
            }

            @Override
            public String toString() {
                return "health-checker";
            }
        };
    }

    public void shutdown() {
        executor.shutdownNow();
    }

}
