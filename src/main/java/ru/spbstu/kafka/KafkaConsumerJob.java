package ru.spbstu.kafka;

import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class KafkaConsumerJob<T> implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerJob.class);

    private final String componentId;
    private final KafkaJobConfiguration jobConfiguration;
    private final Properties consumerProperties;
    private final MessageProcessor<T> messageProcessor;
    private final MessageParser<T> messageParser;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public KafkaConsumerJob(@NotNull String componentId,
                            @NotNull KafkaJobConfiguration jobConfiguration,
                            @NotNull Properties consumerProperties,
                            @NotNull MessageProcessor<T> messageProcessor,
                            @NotNull MessageParser<T> messageParser) {
        Validate.notNull(componentId);
        Validate.notNull(jobConfiguration);
        Validate.notNull(consumerProperties);
        Validate.notNull(messageProcessor);
        Validate.notNull(messageParser);

        this.componentId = componentId;
        this.jobConfiguration = jobConfiguration;
        this.consumerProperties = consumerProperties;
        this.messageProcessor = messageProcessor;
        this.messageParser = messageParser;
    }

    @Override
    public void run() {
        ExecutorService executor = null;
        ConsumerWrapper wrapper = null;
        List<Future<?>> futures = null;
        CountDownLatch threadsCountLatch = null;
        try {
            log.info("Component [{}] kafka is starting kafka consumer", componentId);

            wrapper = ConsumerWrapper.createConsumerWrapper(componentId, jobConfiguration, consumerProperties);
            log.info("Component [{}] consumer wrapper is created with properties: [{}]",
                    componentId, Joiner.on(",").withKeyValueSeparator("=").join(consumerProperties));

            int processingThreadsCount = jobConfiguration.getProcessingThreadsCount();
            executor = Executors.newFixedThreadPool(processingThreadsCount);
            futures = new ArrayList<>(processingThreadsCount);

            while (!closed.get()) {
                ConsumerRecords<Integer, String> records = wrapper.poll();
                int recordsSize = records.count();
                if (recordsSize == 0) {
                    continue;
                }
                log.debug("Component [{}] read [{}] records", componentId, records);
                threadsCountLatch = new CountDownLatch(recordsSize);
                List<ConsumerRecord<Integer, String>> batchOfRecords = new ArrayList<>(recordsSize);
                for (ConsumerRecord<Integer, String> record : records) {
                    batchOfRecords.add(record);
                }

                startProcessingRecords(executor, threadsCountLatch, batchOfRecords, futures::add);
                int processingTimeout = jobConfiguration.getProcessingTimeout();
                if (threadsCountLatch.await(processingTimeout, TimeUnit.SECONDS)) {
                    futures.clear();
                    wrapper.commitOffsets(records);
                    continue;
                }

                shutdownFutures(futures);
                if (threadsCountLatch.await(processingTimeout / 2, TimeUnit.SECONDS)) {
                    futures.clear();
                    continue;
                }

                return;
            }
        } catch (Exception e) {
            shutdownFutures(futures);
            log.error("Error during consuming records from topic [{}], componentId: [{}]",
                    jobConfiguration.getSourceTopics(), componentId, e);
        } finally {
            if (wrapper != null) {
                wrapper.shutdown();
            }
            shutdownExecutor(executor);
        }
    }

    private void startProcessingRecords(@NotNull ExecutorService executor,
                                        @NotNull CountDownLatch threadsCountLatch,
                                        @NotNull List<ConsumerRecord<Integer, String>> batchOfRecords,
                                        @NotNull Consumer<Future<?>> accumulator) {
        for (ConsumerRecord<Integer, String> record : batchOfRecords) {
            RecordJob<T> recordJob = new RecordJob<>(componentId, jobConfiguration.getMaxFailsCount(), closed,
                    threadsCountLatch::countDown, record, messageProcessor, messageParser);
            accumulator.accept(executor.submit(recordJob, null));
        }
    }

    private void shutdownFutures(@Nullable List<Future<?>> futures) {
        if (CollectionUtils.isNotEmpty(futures)) {
            log.info("Component [{}] shutdown processing of job", componentId);
            for (Future<?> future : futures) {
                future.cancel(true);
            }
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null) {
            log.info("Component [{}] shutdown executor", componentId);
            executor.shutdownNow();
        }
    }

}
