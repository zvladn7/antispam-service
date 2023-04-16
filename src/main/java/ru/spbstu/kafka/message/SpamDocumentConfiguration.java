// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.spbstu.kafka.base.KafkaJobConfiguration;

@Configuration
public class SpamDocumentConfiguration implements KafkaJobConfiguration {

    @Value(value = "${antispam.kafka-consumer.message.source-topic}")
    private String sourceTopic;
    @Value(value = "${antispam.kafka-consumer.message.initial-delay-in-seconds}")
    private Long initialDelayInSeconds;
    @Value(value = "${antispam.kafka-consumer.message.consumer-health-check-period-in-seconds}")
    private long consumerHealthCheckPeriodInSeconds;
    @Value(value = "${antispam.kafka-consumer.message.poll-timeout}")
    private Long pollTimeout;
    @Value(value = "${antispam.kafka-consumer.message.custom-group-id}")
    private String customGroupId;
    @Value(value = "${antispam.kafka-consumer.message.max-pool-records}")
    private int maxPoolRecords;
    @Value(value = "${antispam.kafka-consumer.message.processing-threads-count}")
    private int processingThreadsCount;
    @Value(value = "${antispam.kafka-consumer.message.processing-timeout}")
    private int processingTimeout;
    @Value(value = "${antispam.kafka-consumer.message.max-fails-count}")
    private int maxFailsCount;

    @Override
    public String getSourceTopic() {
        return sourceTopic;
    }

    @Override
    public Long getInitialDelayInSeconds() {
        return initialDelayInSeconds;
    }

    @Override
    public long getConsumerHealthCheckPeriodInSeconds() {
        return consumerHealthCheckPeriodInSeconds;
    }

    @Override
    public Long getPollTimeout() {
        return pollTimeout;
    }

    @Override
    public String getCustomGroupId() {
        return customGroupId;
    }

    @Override
    public int getMaxPoolRecords() {
        return maxPoolRecords;
    }

    @Override
    public int getProcessingThreadsCount() {
        return processingThreadsCount;
    }

    @Override
    public int getProcessingTimeout() {
        return processingTimeout;
    }

    @Override
    public int getMaxFailsCount() {
        return maxFailsCount;
    }

}
