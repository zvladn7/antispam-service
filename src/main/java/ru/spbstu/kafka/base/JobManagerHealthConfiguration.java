// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.base;

public class JobManagerHealthConfiguration implements JobManagerConfiguration {

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
