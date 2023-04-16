// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.ip;

public interface SimilarityFunction {
    /**
     * Returns correlation between to difference values. This value is from 0 to 1.
     * If the {@param value1} is equal to {@param value2} the method must return 1.
     */
    double getSimilarity(String value1, String value2);

}
