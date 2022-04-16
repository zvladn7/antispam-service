package ru.spbstu.ip;

import org.springframework.data.util.Pair;

public class CoordinatesUtil {

    private final static double EARTH_DIAMETER = 2 * 6378.2;
    private final static double RAD_CONVERT_VALUE = Math.PI / 180;

    public static double toRadians(double value) {
        return RAD_CONVERT_VALUE * value;
    }

    public static double getCoordinatesDistance(double firstLatitude,
                                                double firstLongitude,
                                                double secondLatitude,
                                                double secondLongitude) {
        double firstLatitudeInRadians = toRadians(firstLatitude);
        double secondLatitudeInRadians = toRadians(secondLatitude);
        double latitudeDiffInRadians = secondLatitudeInRadians - firstLatitudeInRadians;
        double longitudeDiffInRadians = toRadians(secondLongitude - firstLongitude);

        // Find the great circle distance
        double sqrSinDiffLatitude = Math.pow(Math.sin(latitudeDiffInRadians / 2), 2);
        double sqrSinDiffLongitude = Math.pow(Math.sin(longitudeDiffInRadians / 2), 2);
        double tempValue = sqrSinDiffLatitude + Math.cos(firstLatitudeInRadians) * Math.cos(secondLatitudeInRadians) + sqrSinDiffLongitude;
        return EARTH_DIAMETER * Math.atan2(Math.sqrt(tempValue), Math.sqrt(1 - tempValue));
    }

    public static double getCoordinatesDistance2(double lat1,
                                                 double long1,
                                                 double lat2,
                                                 double long2) {
        // Convert the latitudes
        // and longitudes
        // from degree to radians.
        lat1 = toRadians(lat1);
        long1 = toRadians(long1);
        lat2 = toRadians(lat2);
        long2 = toRadians(long2);

        // Haversine Formula
        double dlong = long2 - long1;
        double dlat = lat2 - lat1;

        double ans = Math.pow(Math.sin(dlat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin(dlong / 2), 2);

        ans = 2 * Math.asin(Math.sqrt(ans));

        // Radius of Earth in
        // Kilometers, R = 6371
        // Use R = 3956 for miles
        double R = 6371;

        // Calculate the result
        ans = ans * R;

        return ans;
    }

    public static String getCoordinatesAsString(double latitude, double longitude) {
        return latitude + "," + longitude;
    }

    public static double getCoordinatesDistanceInKm(String coord1, String coord2) {
        if (coord1.equals(coord2)) {
            return 0;
        }

        Pair<Double, Double> firstCoordsPair = parseLatitudeLongitudePair(coord1);
        Pair<Double, Double> secondCoordsPair = parseLatitudeLongitudePair(coord2);

        return getCoordinatesDistance2(
                firstCoordsPair.getFirst(), firstCoordsPair.getSecond(),
                secondCoordsPair.getFirst(), secondCoordsPair.getSecond()
        );
    }

    private static Pair<Double, Double> parseLatitudeLongitudePair(String coords) {
        String[] splittedCoords = coords.split(",");
        return Pair.of(
                Double.parseDouble(splittedCoords[0]),
                Double.parseDouble(splittedCoords[1])
        );
    }

    public static double getCoordinatesSimilarity(String coord1, String coord2, double maxDistanceInKm) {
        double distance = getCoordinatesDistanceInKm(coord1, coord2);
        if (distance >= maxDistanceInKm) {
            return 0;
        }
        return 1. - (distance / maxDistanceInKm);
    }

}
