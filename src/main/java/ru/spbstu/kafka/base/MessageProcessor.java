package ru.spbstu.kafka.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MessageProcessor<T> {

    void process(@NotNull Map<String, List<T>> messages) throws Exception;

}
