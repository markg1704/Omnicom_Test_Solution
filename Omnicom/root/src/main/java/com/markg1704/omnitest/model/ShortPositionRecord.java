package com.markg1704.omnitest.model;

import java.time.Instant;

/*
Class ShortPositionRecord

Object for storing position and time-date information from a PositionRecord.
Object has less memory requirement than a standard PositionRecord allowing for in-memory
processing of the large datasets.

Standard getter - setter methods for each variable.

Note : dateTime variable here is stored as an Instant.

 */

public class ShortPositionRecord {

    private double longitude;
    private double latitude;
    private Instant timeValue;

    public ShortPositionRecord() {
       //default constructor
    }

    public ShortPositionRecord(double longitude, double latitude,
                               Instant timeValue) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeValue = timeValue;

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Instant getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Instant timeValue) {
        this.timeValue = timeValue;
    }

    @Override
    public String toString() {
        return "ShortPositionRecord{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", timeValue=" + timeValue +
                '}';
    }
}
