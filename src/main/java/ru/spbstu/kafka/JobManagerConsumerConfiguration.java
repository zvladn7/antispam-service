package ru.spbstu.kafka;

public class JobManagerConsumerConfiguration implements JobManagerConfiguration {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public long getInitialDelayInSecond() {
        return 0;
    }

    @Override
    public Long getPeriod() {
        return null;
    }

}
