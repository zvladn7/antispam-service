package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class KafkaPublisher<T> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);

    private final ObjectMapper mapper;
    private final String topic;
    private final Properties producerConfiguration;
    private final Function<T, Integer> partitionKeySupplier;
    private volatile KafkaProducer<Integer, String> producer;

    public KafkaPublisher(@NotNull ObjectMapper mapper,
                          @NotNull String topic,
                          @NotNull Properties producerConfiguration,
                          @NotNull Function<T, Integer> partitionKeySupplier) {
        Validate.notNull(mapper);
        Validate.notNull(topic);
        Validate.notNull(producerConfiguration);
        Validate.notNull(partitionKeySupplier);

        this.mapper = mapper;
        this.topic = topic;
        this.producerConfiguration = producerConfiguration;
        this.partitionKeySupplier = partitionKeySupplier;
    }

    public synchronized void refresh() {
        try {
            KafkaProducer<Integer, String> oldProducer = producer;
            producer = new KafkaProducer<>(producerConfiguration);
            if (oldProducer != null) {
                oldProducer.close();
            }
        } catch (Exception e) {
            logger.error("Failed to refresh producer for topic: [{}]", topic, e);
        }
    }

    public boolean publish(@NotNull T item) {
        try {
            String serializedItem = serializeItem(item);
            logger.debug("pushing a message in topic [{}]: [{}]", topic, serializedItem);
            List<PartitionInfo> partitionInfos = producer.partitionsFor(topic);
            Integer partitionCount = CollectionUtils.isEmpty(partitionInfos) ? null : partitionInfos.size();
            ProducerRecord<Integer, String> message = new ProducerRecord<>(
                    topic,
                    partitionCount == null ? null : partitionKeySupplier.apply(item) % partitionCount,
                    null,
                    serializedItem);
            producer.send(message);
            return true;
        } catch (Exception e) {
            logger.warn("Failed to publish item: [{}] to topic: [{}]", item, topic);
            return false;
        }
    }

    public void shutdown() {
        if (producer != null) {
            producer.close();
        }
    }

    private String serializeItem(@NotNull T item) {
        try {
            return mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            logger.warn("Unable to serialize item: [{}]", item, e);
            throw new UncheckedIOException(e);
        }
    }

}
