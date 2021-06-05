package com.eggdevs.thequakeseeker.data;

import java.io.Serializable;

public class CityDetails implements Serializable {

    private String mLocation, mUrl;
    private double mMagnitude;
    private long timeInMilliSeconds;
    private String feltBy;

    public CityDetails(double mMagnitude, String mLocation, long timeInMilliSeconds, String mUrl,
                       String mFeltBy) {
        this.mLocation = mLocation;
        this.timeInMilliSeconds = timeInMilliSeconds;
        this.mMagnitude = mMagnitude;
        this.mUrl = mUrl;
        this.feltBy = mFeltBy;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliSeconds() {
        return timeInMilliSeconds;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFeltBy() {
        return feltBy;
    }
}
