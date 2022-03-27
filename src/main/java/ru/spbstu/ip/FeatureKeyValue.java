package ru.spbstu.ip;

public class FeatureKeyValue {

    private final IpFeature feature;
    private final long key;
    private final double value;

    public FeatureKeyValue(IpFeature feature,
                           long key,
                           double value) {
        this.feature = feature;
        this.key = key;
        this.value = value;
    }

    public IpFeature getFeature() {
        return feature;
    }

    public long getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

}
