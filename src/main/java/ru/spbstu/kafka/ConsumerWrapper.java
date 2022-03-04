package ru.spbstu.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;

public class ConsumerWrapper {

    private static final Logger log = LoggerFactory.getLogger(ConsumerWrapper.class);

    private final String consumerId;
    private final KafkaConsumer<Integer, String> consumer;
    private final KafkaJobConfiguration kafkaJobConfiguration;

    ConsumerWrapper(@NotNull String consumerId,
                    @NotNull KafkaConsumer<Integer, String> consumer,
                    @NotNull KafkaJobConfiguration kafkaJobConfiguration) {
        Validate.notNull(consumerId);
        Validate.notNull(consumer);
        Validate.notNull(kafkaJobConfiguration);
        this.consumerId = consumerId;
        this.consumer = consumer;
        this.kafkaJobConfiguration = kafkaJobConfiguration;
    }

    void commitOffsets(@NotNull ConsumerRecords<Integer, String> records) {
        Validate.notNull(records);

        consumer.commitSync();
    }

    ConsumerRecords<Integer, String> poll() {
        return consumer.poll(getPollTimeout());
    }

    long getPollTimeout() {
        Long value = kafkaJobConfiguration.getPollTimeout();
        return value == null || value < 0 ? 0 : value;
    }

    void shutdown() {
        if (consumer != null) {
            log.info("Consumer [{}] shutdowning consumer", consumerId);
        }
    }

    static ConsumerWrapper createConsumerWrapper(@NotNull String componentId,
                                                 @NotNull KafkaJobConfiguration kafkaJobConfiguration,
                                                 @NotNull Properties consumerProperties) {
        Validate.notNull(componentId);
        Validate.notNull(kafkaJobConfiguration);

        Properties properties = new Properties();
        properties.putAll(consumerProperties);

        String consumerGroupId = null;
        if (StringUtils.isNotBlank(kafkaJobConfiguration.getCustomGroupId())) {
            log.info("Using custom groupId=[{}] for [{}]", kafkaJobConfiguration.getCustomGroupId(), componentId);
            consumerGroupId = kafkaJobConfiguration.getCustomGroupId();
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        }
        if (StringUtils.isBlank(consumerGroupId)) {
            consumerGroupId = properties.getProperty(ConsumerConfig.GROUP_ID_CONFIG);
        }
        // This is the place where we are sure about consumer group name
        Validate.notBlank(consumerGroupId);

        if (!properties.containsKey(ConsumerConfig.MAX_POLL_RECORDS_CONFIG)) {
            properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaJobConfiguration.getMaxPoolRecords());
        }
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(kafkaJobConfiguration.getSourceTopics(), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                log.info("Topic partitions revoked {}", partitions);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                log.info("Topic partitions assigned {}", partitions);
            }
        });

        return new ConsumerWrapper(consumerGroupId, consumer, kafkaJobConfiguration);
    }

}
