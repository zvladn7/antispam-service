package ru.spbstu.kafka;

import org.jetbrains.annotations.NotNull;

public interface MessageParser<T> {

    @NotNull
    T parse(@NotNull String kafkaMessage);

}
