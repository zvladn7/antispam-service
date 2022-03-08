package ru.spbstu.kafka.base;

public interface JobManagerConfiguration {

    boolean isEnabled();

    long getInitialDelayInSecond();

    Long getPeriod();

}
