package ru.spbstu.kafka;

public interface JobManagerConfiguration {

    boolean isEnabled();

    long getInitialDelayInSecond();

    Long getPeriod();

}
