// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

public interface JobManagerConfiguration {

    boolean isEnabled();

    long getInitialDelayInSecond();

    Long getPeriod();

}
