package ru.spbstu.ip;

import com.google.common.collect.ImmutableMap;
import ru.spbstu.storage.ip.GeoIP;

public enum IpFeature implements Feature {
    COORDINATES() {
        @Override
        public String getKey(PreparedIpEntry preparedIpEntry) {
            return preparedIpEntry.getCoordinates();
        }

        @Override
        public String getValueString(PreparedIpEntry preparedIpEntry) {
            IpEntry ipEntry = preparedIpEntry.getIpEntry();
            GeoIP geoIPInfo = ipEntry.getGeoIPInfo();
            return geoIPInfo.getLongitude() + ":" + geoIPInfo.getLatitude() + ":" + geoIPInfo.getCity();
        }
    };

    private static final int MAX_DISTANCE_IN_KM = 1000;

    public static ImmutableMap<IpFeature, SimilarityFunction> SIMILARITY_FUNCTIONS = ImmutableMap.<IpFeature, SimilarityFunction>builder()
            .put(IpFeature.COORDINATES, (value1, value2) -> CoordinatesUtil.getCoordinatesSimilarity(value1, value2, MAX_DISTANCE_IN_KM))
            .build();

    private static final int amountOfFeature = IpFeature.values().length;

    private static int size() {
        return amountOfFeature;
    }

    public double getValue(String key) {
        return 1;
    }

}
