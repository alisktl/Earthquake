package com.ru.alisher.earthquake.earthquake.data;

/**
 * Created by alisher on 2/7/18.
 */

public class Earthquake {

    // Id of the earthquake in https://earthquake.usgs.gov
    private String mId;

    // Magnitude of the earthquake
    private double mMagnitude;

    // Mercalli Intensity Scale of the earthquake
    private double mMercalliIntensityScale;

    // Alert Level of the earthquake
    private String mAlertLevel;

    // Latitude of the earthquake
    private double mLat;

    // Longitude of the earthquake
    private double mLon;

    // Location (name of the place) of the earthquake
    private String mLocation;

    // Offset Location of the earthquake
    private String mOffsetLocation;

    // Time in milliseconds of the earthquake
    private long mTimeInMilliseconds;

    // URL of the detailed information of the earthquake
    private String mDetailUrl;

    /**
     *
     * @param id is the id of the earthquake
     * @param magnitude is the magnitude of the earthquake
     * @param mercalliIntensityScale is the Mercalli Intensity Scale of the earthquake
     * @param alertLevel is the alert level of the earthquake
     * @param lat is the latitude of the earthquake
     * @param lon is the longitude of the earthquake
     * @param location is the location of the earthquake
     * @param locationOffset is the location offset of the earthquake
     * @param timeInMilliseconds is the time in milliseconds of the earthquake
     * @param detailUrl is the URL of the detailed information of the earthquake
     */
    public Earthquake(String id, double magnitude, double mercalliIntensityScale, String alertLevel, double lat,
                      double lon, String location, String locationOffset, long timeInMilliseconds, String detailUrl) {
        this.mId = id;
        this.mMagnitude = magnitude;
        this.mMercalliIntensityScale = mercalliIntensityScale;
        this.mAlertLevel = alertLevel;
        this.mLat = lat;
        this.mLon = lon;
        this.mLocation = location;
        this.mOffsetLocation = locationOffset;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mDetailUrl = detailUrl;
        this.mAlertLevel = alertLevel;
    }

    public String getId() {
        return mId;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public double getMercalliIntensityScale() {
        return mMercalliIntensityScale;
    }

    public String getAlertLevel() {
        return mAlertLevel;
    }

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLon;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getLocationOffset() {
        return mOffsetLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getDetailUrl() {
        return mDetailUrl;
    }
}
