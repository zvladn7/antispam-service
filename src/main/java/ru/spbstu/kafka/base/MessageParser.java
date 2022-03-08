package ru.spbstu.kafka.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;

public interface MessageParser<T> {

    @NotNull
    T parse(@NotNull String kafkaMessage) throws JsonProcessingException;

}
