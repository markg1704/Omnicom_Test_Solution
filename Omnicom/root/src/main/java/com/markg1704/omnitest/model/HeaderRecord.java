package com.markg1704.omnitest.model;

import java.time.Instant;

/*
Class HeaderRecord

Simple class to store static variables containing header information required for the time, date and
position calculations.

Variables;

static long imageSampleRate - stores sample rate in nannoseconds

static Instant timeZero - stores Tzero as computed from the .net variable in the positions .csv file.

Storing timeZero in a header file stops data redundancy in the processed position record in the database.

 */

public class HeaderRecord {

    private static long imageSampleRate;
    private static Instant timeZero;

    public static long getImageSampleRate() {
        return imageSampleRate;
    }

    public static void setImageSampleRate(long sampleRate) {
        imageSampleRate = sampleRate;
    }

    public static Instant getTimeZero() {
        return timeZero;
    }

    public static void setTimeZero(Instant instant) {
        timeZero = instant;
    }


}
