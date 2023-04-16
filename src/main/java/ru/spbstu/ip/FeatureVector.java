// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.ip;

import java.util.HashMap;
import java.util.Map;

public class FeatureVector {

    private Map<String, Double> values = new HashMap<>();
    private double length;

    public Map<String, Double> getValues() {
        return values;
    }

    public void add(FeatureKeyValue featureKeyValue) {
        add(featureKeyValue.getKey(), featureKeyValue.getValue());
    }

    public void add(String key, double value) {
        if (value != 0) {
            values.put(key, value);
            length = 0;
        }
    }

}
