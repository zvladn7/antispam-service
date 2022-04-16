package ru.spbstu.kafka.base;

import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordJob<T> implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RecordJob.class);

    private final String componentId;
    private final int maxFailsCount;
    private final AtomicBoolean closed;
    private final Runnable recordProcessedCallback;
    private final ConsumerRecord<Integer, String> record;
    private final MessageProcessor<T> messageProcessor;
    private final MessageParser<T> messageParser;

    public RecordJob(@NotNull String componentId,
                     int maxFailsCount,
                     @NotNull AtomicBoolean closed,
                     @NotNull Runnable recordProcessedCallback,
                     @NotNull ConsumerRecord<Integer, String> record,
                     @NotNull MessageProcessor<T> messageProcessor,
                     @NotNull MessageParser<T> messageParser) {
        Validate.notNull(componentId);
        Validate.notNull(closed);
        Validate.notNull(recordProcessedCallback);
        Validate.notNull(record);
        Validate.notNull(messageProcessor);
        Validate.notNull(messageParser);

        this.componentId = componentId;
        this.maxFailsCount = maxFailsCount;
        this.closed = closed;
        this.recordProcessedCallback = recordProcessedCallback;
        this.record = record;
        this.messageProcessor = messageProcessor;
        this.messageParser = messageParser;
    }

    @Nullable
    T parse(@NotNull ConsumerRecord<Integer, String> record) {
        Validate.notNull(record);
        try {
            log.debug("Component [{}] parsing record \n [{}]", componentId, record);
            String message = record.value();
            return messageParser.parse(message);
        } catch (Exception e) {
            log.warn("Component [{}] failed to parse record \n [{}]", componentId, record);
            return null;
        }
    }

    @Override
    public void run() {
        boolean processed = false;
        try {
            if (closed.get()) {
                return;
            }
            T event = parse(record);
            if (event == null) {
                return;
            }
            if (!processEventWithRetries(event, record.topic())) {
                log.error("failed {} times. Skipping event [{}] for record [{}], component [{}]",
                        maxFailsCount, event, record, componentId);
            } else {
                processed = true;
            }
        } catch (Exception e) {
            log.error("Component [{}] failed during consuming record \n [{}]", componentId, record, e);
            closed.set(true);
        } finally {
            recordProcessedCallback.run();
        }
    }

    private boolean processEventWithRetries(@NotNull T event,
                                            @NotNull String topic) {
        int failsCount = 0;
        while (!closed.get() && failsCount < maxFailsCount) {
            try {
                messageProcessor.process(Collections.singletonMap(topic, Collections.singletonList(event)));
                return true;
            } catch (Exception e) {
                failsCount++;
                log.error("Component [{}] failed to process event: [{}]", componentId, event, e);
            }
        }
        return failsCount != maxFailsCount;
    }
}
