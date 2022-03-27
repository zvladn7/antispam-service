package ru.spbstu.ip;

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

    public static long getCoordinatesAsLong(double latitude, double longitude) {
        return Double.doubleToLongBits(latitude) | Double.doubleToLongBits(longitude) & 0xFFFFFFFL;
    }

    public static double getCoordinatesDistanceInKm(long coord1, long coord2) {
        if (coord1 == coord2) {
            return 0;
        }
        return getCoordinatesDistance(
                Double.longBitsToDouble((int) (coord1 >> 32)), Double.longBitsToDouble((int) coord1),
                Double.longBitsToDouble((int) (coord2 >> 32)), Double.longBitsToDouble((int) coord2)
        );
    }

    public static double getCoordinatesSimilarity(long coord1, long coord2, double maxDistanceInKm) {
        double distance = getCoordinatesDistanceInKm(coord1, coord2);
        if (distance >= maxDistanceInKm) {
            return 0;
        }
        return 1. - (distance / maxDistanceInKm);
    }

}
