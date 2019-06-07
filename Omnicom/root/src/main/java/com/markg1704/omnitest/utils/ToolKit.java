package com.markg1704.omnitest.utils;

import java.time.Instant;

/*
Class ToolKit

Class containing static methods for computations required by the main program.

Methods;

public static Instant convertDotNetTicks(long tickValue) - Converts .net tick to a Java Instant

public static double latLongRangeCalculation(double latA, double longA, double latB, double longB)
A method for deriving the distance between two Lat/Long coordinates.
The function takes in decimal lat & long from 2 points and computes distance between the two
using the Haversine approximation see https://en.wikipedia.org/wiki/Haversine_formula

The method was used during initial testing to try to help determine the units of measurement for the
distance field in the positions.csv file.  It is an approximation but it is the best you can do without
geodetic information.

 */

public class ToolKit {

    public static Instant convertDotNetTicks(long tickValue) {

        long TICKS_AT_EPOCH = 621355968000000000L;
        long TICKS_PER_MILLISECOND = 10000L;

        long millisecondsFromEpoch = (tickValue - TICKS_AT_EPOCH)/TICKS_PER_MILLISECOND;
        Instant instant = Instant.ofEpochMilli(millisecondsFromEpoch);

        return instant;

    }

    public static double latLongRangeCalculation(double latA, double longA, double latB, double longB) {

        double EARTH_RADIUS = 6378137; //radius of the earth (at the equator) in metres
        double latARadian = Math.toRadians(latA);
        double latBRadian = Math.toRadians(latB);

        double deltaLat = Math.toRadians(latB - latA);
        double deltaLong = Math.toRadians(longB - longA);

        double alpha = Math.pow(Math.sin(deltaLat/2),2) + Math.cos(latARadian) * Math.cos(latBRadian) * Math.pow(Math.sin(deltaLong),2);
        double beta = 2 * Math.atan2(Math.sqrt(alpha), Math.sqrt(1-alpha));

        return Math.abs(EARTH_RADIUS * beta);

    }


}
