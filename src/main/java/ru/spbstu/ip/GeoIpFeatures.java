package ru.spbstu.ip;

import java.io.Serializable;

public class GeoIpFeatures implements Serializable {

    private String coordinates;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}
