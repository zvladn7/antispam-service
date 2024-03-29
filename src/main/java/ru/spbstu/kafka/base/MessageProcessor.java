// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MessageProcessor<T> {

    void process(@NotNull Map<String, List<T>> messages);

}
