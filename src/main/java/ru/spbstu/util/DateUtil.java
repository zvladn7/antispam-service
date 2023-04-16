// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.util;

public class DateUtil {

    public static int currentDateCompact() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
