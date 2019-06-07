package com.markg1704.omnitest.model;

/*
Class DatabaseSchema

This class declares a series of public static String variables which define the database schema.
The variables are used to build Sql statements and queries.

The class is declared as final so it cannot be extended.  The constructor is declared as private
so it cannot be instantiated.

 */

public final class DatabaseSchema {

    private DatabaseSchema() {
        //
        //private constructor - class cannot be instantiated
        //
    }

    public static final String POSITION_TABLE = "positions";

    public static final String POSITION_ID = "id";
    public static final String POSITION_LONGITUDE = "longitude";
    public static final String POSITION_LATITUDE = "latitude";
    public static final String POSITION_DISTANCE = "distance_meters";
    public static final String POSITION_VELOCITY = "velocity_meterspersecond";
    public static final String POSITION_DATETIME = "date_time";

    public static final String IMAGE_TABLE = "images";

    public static final String IMAGE_ID = "id";
    public static final String IMAGE_OFFSET = "offset";
    public static final String IMAGE_LENGTH = "length";
    public static final String IMAGE_HOUR = "hour";
    public static final String IMAGE_MINUTE = "minute";
    public static final String IMAGE_SECOND = "second";
    public static final String IMAGE_FRAME = "frame";
    public static final String IMAGE_LONGITUDE = "longitude";
    public static final String IMAGE_LATITUDE = "latitude";
    public static final String IMAGE_DATETIME = "date_time";

}
