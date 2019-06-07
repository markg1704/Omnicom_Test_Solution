package com.markg1704.omnitest.model;

/*
Class ImageRecord

Object for storing image records defined in the image .csv file.  Additional field included in object
for storing position and time information.  Variables named as per .csv header.

Standard getter - setter methods for each variable.

Note : dateTime variable stored as a String as SQLite database does not directly handle date-time objects.

 */

public class ImageRecord {

    private int id;  // primary key
    private long offset;
    private long length;
    private int hour;
    private int minute;
    private int second;
    private int frame;
    private double longitude;
    private double latitude;
    private String dateTime;

    public ImageRecord() {

        //default constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ImageRecord{" +
                "offset=" + offset +
                ", length=" + length +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", frame=" + frame +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
