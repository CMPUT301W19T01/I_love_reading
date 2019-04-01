package com.example.libo.myapplication.Model;

import java.io.Serializable;

/**
 * The type Lat lng.
 */
public class LatLng implements Serializable {
    private Double latitude;
    private Double longitude;

    /**
     * Instantiates a new Lat lng.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Instantiates a new Lat lng.
     */
    public LatLng(){

    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
