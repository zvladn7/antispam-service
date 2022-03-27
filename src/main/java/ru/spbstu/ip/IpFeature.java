package ru.spbstu.ip;

import com.google.common.collect.ImmutableMap;

public enum IpFeature implements Feature {
    COORDINATES() {
        @Override
        public long getKey() {
            return 0;
        }

        @Override
        public String getValueString() {
            return null;
        }
    };

    private static final int MAX_DISTANCE_IN_KM = 1000;

    private static ImmutableMap<IpFeature, SimilarityFunction> SIMILARITY_FUNCTIONS = ImmutableMap.<IpFeature, SimilarityFunction>builder()
            .put(IpFeature.COORDINATES, (value1, value2) -> CoordinatesUtil.getCoordinatesSimilarity(value1, value2, MAX_DISTANCE_IN_KM))
            .build();

}
