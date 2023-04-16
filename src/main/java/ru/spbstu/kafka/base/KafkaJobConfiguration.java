// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

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
