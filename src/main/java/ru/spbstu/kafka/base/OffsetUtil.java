// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OffsetUtil {

    private OffsetUtil() {}

    public static <K, V> Map<TopicPartition, Long> mapRecordsToOffsets(@NotNull Iterable<ConsumerRecord<K, V>> records) {
        Map<TopicPartition, Long> offsets = new HashMap<>();
        for (ConsumerRecord<K, V> consumerRecord : records) {
            offsets.merge(
                    new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                    consumerRecord.offset(),
                    Long::max
            );
        }
        return offsets;
    }

}
