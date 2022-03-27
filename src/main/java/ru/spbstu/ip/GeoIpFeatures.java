package ru.spbstu.ip;

import java.io.Serializable;

public class GeoIpFeatures implements Serializable {

    private long coordinates = 0;

    public long getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(long coordinates) {
        this.coordinates = coordinates;
    }

}
