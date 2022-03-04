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

    private final boolean consumerEnabled;
    private final Set<String> sourceTopics;
    private final boolean consumerHealthCheckerEnabled;
    private final long consumerHeathCheckPeriodInSeconds;
    private final Long pollTimeout;
    private final String customGroupId;
    private final int maxPoolRecords;

}
