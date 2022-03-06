package ru.spbstu.kafka;

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


    }

}
