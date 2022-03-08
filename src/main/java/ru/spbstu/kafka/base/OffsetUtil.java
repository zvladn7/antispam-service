package ru.spbstu.kafka.base;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OffsetUtil {

    public static <K, V> Map<TopicPartition, Long> mapRecordsToOffsets(@NotNull Iterable<ConsumerRecord<K, V>> records) {
        Map<TopicPartition, Long> offsets = new HashMap<>();
        for (ConsumerRecord<K, V> record : records) {
            offsets.merge(
                    new TopicPartition(record.topic(), record.partition()),
                    record.offset(),
                    Long::max
            );
        }
        return offsets;
    }

}
