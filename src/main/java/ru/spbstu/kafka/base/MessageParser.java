// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;

public interface MessageParser<T> {

    @NotNull
    T parse(@NotNull String kafkaMessage) throws JsonProcessingException;

}
