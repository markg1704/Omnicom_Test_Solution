package com.markg1704.omnitest.model;

/*
Class PositionRecord

Object for storing position records defined in the positions .csv file.  Variables named as per .csv header.
The fields have been reduced from the imput .csv file to avoid data redundancy in the database.  Only required data
is stored.

Standard getter - setter methods for each variable.

Note : dateTime variable stored as a String as SQLite database does not directly handle date-time objects.

 */

public class PositionRecord {

    private int id; // primary key
    private double longitude;
    private double latitude;
    private double distance;
    private double velocity;
    private String dateTime;

    public PositionRecord() {
        //default constructor

    }

    public PositionRecord(double longitude, double latitude,
                          double distance, double velocity) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.velocity = velocity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "PositionRecord{" +
                "id=" + id +
                ", longitude=" + this.longitude +
                ", latitude=" + this.latitude +
                ", distance=" + this.distance +
                ", velocity=" + this.velocity +
                ", dateTime=" + this.dateTime +
                '}';
    }
}
