package ru.spbstu.ip;

public interface Feature {

    long getKey(PreparedIpEntry preparedIpEntry);

    String getValueString(PreparedIpEntry preparedIpEntry);

}
