package ru.spbstu.kafka;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.Set;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class KafkaJobConfiguration {

    private final Set<String> sourceTopics;
    private final Long initialDelayInSeconds;
    private final long consumerHeathCheckPeriodInSeconds;
    private final Long pollTimeout;
    private final String customGroupId;
    private final int maxPoolRecords;
    private final int processingThreadsCount;
    private final int processingTimeout;
    private final int maxFailsCount;

}
