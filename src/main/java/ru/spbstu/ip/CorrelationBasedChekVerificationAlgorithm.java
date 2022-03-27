package ru.spbstu.ip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CorrelationBasedChekVerificationAlgorithm {

    private static final List<IpFeature> features = Collections.singletonList(IpFeature.COORDINATES);

    public CheckIpResult checkIP(@Nullable IpEntryList ipEntryList,
                                 @NotNull IpEntry newIpEntry) {
        PreparedIpEntry newPreparedEntry = PreparedIpEntryFactory.prepare(newIpEntry);
        fillFeatures(newPreparedEntry);
        List<PreparedIpEntry> preparedIpEntries = prepare(ipEntryList);
        fillFeatures(preparedIpEntries);

        double correlation;
        if (preparedIpEntries.isEmpty()) {
            correlation = 1;
        } else {
            correlation = getCorrelation(preparedIpEntries, newPreparedEntry);
        }
        return new CheckIpResult(correlation);
    }

    private List<PreparedIpEntry> prepare(@Nullable IpEntryList ipEntryList) {
        if (ipEntryList == null) {
            return Collections.emptyList();
        }
        List<PreparedIpEntry> preparedEntryList
                = PreparedIpEntryFactory.prepare(ipEntryList.getIpEntries());
        fillFeatures(preparedEntryList);
        return preparedEntryList;
    }

    private void fillFeatures(List<PreparedIpEntry> preparedEntryList) {
        for (PreparedIpEntry preparedIpEntry : preparedEntryList) {
            fillFeatures(preparedIpEntry);
        }
    }

    private void fillFeatures(@NotNull PreparedIpEntry preparedIpEntry) {
        preparedIpEntry.setFeatureKeyValues(getFeatureKeyValues(preparedIpEntry, features));
    }

    public static List<FeatureKeyValue> getFeatureKeyValues(PreparedIpEntry entry, List<IpFeature> features) {
        ArrayList<FeatureKeyValue> featureKeyValues = new ArrayList<FeatureKeyValue>();
        for (IpFeature feature : features) {
            FeatureKeyValue featureKeyValue = new FeatureKeyValue(feature, feature.getKey(entry), feature.getValue(feature.getKey(entry)));
            featureKeyValues.add(featureKeyValue);
        }
        return featureKeyValues;
    }

    private double getCorrelation(List<PreparedIpEntry> ipEntries, PreparedIpEntry newEntry) {
        return getCorrelation(createCluster(ipEntries), newEntry);
    }

    private double getCorrelation(IpCluster ipCluster, PreparedIpEntry newEntry) {
        if (ipCluster.isEmpty()) {
            return 0;
        }
        return getCorrelation(ipCluster.getVector(), newEntry.getFeatureKeyValues());
    }

    private double getCorrelation(@NotNull Map<IpFeature, FeatureVector> vector,
                                  @NotNull List<FeatureKeyValue> featureKeyValues) {
        double bestWeight = 0;
        for (FeatureKeyValue featureKeyValue : featureKeyValues) {
            if (featureKeyValue.getValue() > 0) {
                SimilarityFunction similarityFunction = IpFeature.SIMILARITY_FUNCTIONS.get(featureKeyValue.getFeature());
                FeatureVector featureVector = vector.get(featureKeyValue.getFeature());
                if (featureVector == null) {
                    continue;
                }
                Set<Long> featureKeys = featureVector.getValues().keySet();
                long newFeatureKey = featureKeyValue.getKey();
                if (featureKeys.contains(newFeatureKey)) {
                    bestWeight = 1;
                } else if (similarityFunction != null) {
                    double maxSimilarity = getMaxSimilarity(similarityFunction, featureKeys, newFeatureKey);
                    if (maxSimilarity > bestWeight) {
                        bestWeight = maxSimilarity;
                    }
                }
            }
        }
        return bestWeight;
    }

    private double getMaxSimilarity(@NotNull SimilarityFunction similarityFunction,
                                    @NotNull Set<Long> featureKeys,
                                    long newFeatureKey) {
        double maxSimilarity = 0;
        for (Long key : featureKeys) {
            maxSimilarity = Math.max(maxSimilarity, similarityFunction.getSimilarity(key, newFeatureKey));
            if (maxSimilarity >= 1) {
                break;
            }
        }
        return maxSimilarity;
    }

    private IpCluster createCluster(@NotNull List<PreparedIpEntry> ipEntries) {
        IpCluster cluster = new IpCluster();
        for (PreparedIpEntry entry : ipEntries) {
            cluster.add(entry);
        }
        return cluster;
    }

}
