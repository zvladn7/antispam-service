package ru.spbstu.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IpCluster {

    private Map<IpFeature, FeatureVector> vector = new HashMap<>();
    private final List<PreparedIpEntry> entries = new ArrayList<>();
    private boolean verified;
    private int firstTime = Integer.MAX_VALUE;
    private int lastTime;

    public void add(PreparedIpEntry entry) {
        entries.add(entry);
        addFeatures(entry.getFeatureKeyValues());
        int entryFirstTime = entry.getIpEntry().getFirstTime();
        int entryLastTime = entry.getIpEntry().getLastTime();
        this.firstTime = Math.min(firstTime, entryFirstTime);
        this.lastTime = Math.max(lastTime, entryLastTime);
        this.verified |= entry.getIpEntry().isVerified();
    }

    private void addFeatures(List<FeatureKeyValue> featureKeyValues) {
        for (FeatureKeyValue featureKeyValue : featureKeyValues) {
            if (featureKeyValue.getValue() > 0) {
                IpFeature feature = featureKeyValue.getFeature();
                FeatureVector featureVector = vector.get(feature);
                if (featureVector == null) {
                    featureVector = new FeatureVector();
                    vector.put(feature, featureVector);
                }
                featureVector.add(featureKeyValue);
            }
        }
    }

    public Map<IpFeature, FeatureVector> getVector() {
        return vector;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

}
