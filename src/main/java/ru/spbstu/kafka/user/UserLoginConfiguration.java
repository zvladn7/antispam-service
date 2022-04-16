package ru.spbstu.kafka.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.spbstu.kafka.base.KafkaJobConfiguration;

@Configuration
public class UserLoginConfiguration implements KafkaJobConfiguration {

    @Value(value = "${antispam.kafka-consumer.userLogin.source-topic}")
    private String sourceTopic;
    @Value(value = "${antispam.kafka-consumer.userLogin.initial-delay-in-seconds}")
    private Long initialDelayInSeconds;
    @Value(value = "${antispam.kafka-consumer.userLogin.consumer-health-check-period-in-seconds}")
    private long consumerHealthCheckPeriodInSeconds;
    @Value(value = "${antispam.kafka-consumer.userLogin.poll-timeout}")
    private Long pollTimeout;
    @Value(value = "${antispam.kafka-consumer.userLogin.custom-group-id}")
    private String customGroupId;
    @Value(value = "${antispam.kafka-consumer.userLogin.max-pool-records}")
    private int maxPoolRecords;
    @Value(value = "${antispam.kafka-consumer.userLogin.processing-threads-count}")
    private int processingThreadsCount;
    @Value(value = "${antispam.kafka-consumer.userLogin.processing-timeout}")
    private int processingTimeout;
    @Value(value = "${antispam.kafka-consumer.userLogin.max-fails-count}")
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
