package ru.spbstu.kafka.base;

import java.util.Set;

public interface KafkaJobConfiguration {

    String getSourceTopic();

    Long getInitialDelayInSeconds();

    long getConsumerHealthCheckPeriodInSeconds();

    Long getPollTimeout();

    String getCustomGroupId();

    int getMaxPoolRecords();

    int getProcessingThreadsCount();

    int getProcessingTimeout();

    int getMaxFailsCount();

}
