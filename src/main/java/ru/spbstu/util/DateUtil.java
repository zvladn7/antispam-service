package ru.spbstu.util;

public class DateUtil {

    public static int currentDateCompact() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
