package ru.spbstu.ip;

import java.util.HashMap;
import java.util.Map;

public class FeatureVector {

    private Map<Long, Double> values = new HashMap<>();
    private double length;

    public Map<Long, Double> getValues() {
        return values;
    }

    public void add(FeatureKeyValue featureKeyValue) {
        add(featureKeyValue.getKey(), featureKeyValue.getValue());
    }

    public void add(long key, double value) {
        if (value != 0) {
            values.put(key, value);
            length = 0;
        }
    }

}
