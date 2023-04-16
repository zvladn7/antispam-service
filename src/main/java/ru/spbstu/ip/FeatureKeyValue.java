// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.ip;

public class FeatureKeyValue {

    private final IpFeature feature;
    private final String key;
    private final double value;

    public FeatureKeyValue(IpFeature feature,
                           String key,
                           double value) {
        this.feature = feature;
        this.key = key;
        this.value = value;
    }

    public IpFeature getFeature() {
        return feature;
    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

}
