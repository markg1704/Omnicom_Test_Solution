package com.markg1704.omnitest.utils;

import com.markg1704.omnitest.model.HeaderRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
Class TimeAnalysisTools

A class of static methods providing tools for the processing and analysis of the position & image records.

Contains only one method; public static List<ShortPositionRecord> interpolatePositionRecords(List<ShortPositionRecord> rawDataList)

public static List<ShortPositionRecord> interpolatePositionRecords(List<ShortPositionRecord> rawDataList)

Method accepts a list of ShortPositionRecord objects and re-samples as a time series with a sample rate
equal to that defined in the HeaderRecord.  Longitude & Latitude values are interpolated, by simple linear interpolation,
to the new time discretely sampled series.

The method outputs a new ArrayList<ShortPositionRecord> of the interpolated values for use in processing of the image
.csv file.

 */

public class TimeAnalysisTools {

    public static List<ShortPositionRecord> interpolatePositionRecords(List<ShortPositionRecord> rawDataList) {

        List<ShortPositionRecord> processedList = new ArrayList<>();  //initialise ArrayList for interpolated data

        long sampleRate = HeaderRecord.getImageSampleRate(); // set sample rate

        Instant timeLastSample = rawDataList.get(rawDataList.size()-1).getTimeValue(); //read last record in raw data file to get end time for processing

        long timeDifference = Duration.between(HeaderRecord.getTimeZero(), timeLastSample).toNanos(); //convert Instant to time difference in nannoseconds

        long numberOfSamples = (timeDifference / sampleRate) + 1; //compute the number of samples required to sample from Tzero to Tend.

        System.out.printf("\nCreating positioning time series with sample rate of %4d ms.\n\n", HeaderRecord.getImageSampleRate() / 1_000_000L);

        //generate ArrayList of ShortPositionRecord objects.  Populate the dateTime field with an Instant from TZero in steps of sampleRate
        for(int j = 0; j < numberOfSamples; j++) {
            ShortPositionRecord shortPositionRecord = new ShortPositionRecord();
            shortPositionRecord.setTimeValue(HeaderRecord.getTimeZero().plusNanos(j * sampleRate));
            //at the first record in the series, write in the lat / long data
            if(j == 0) {
                shortPositionRecord.setLongitude(rawDataList.get(0).getLongitude());
                shortPositionRecord.setLatitude(rawDataList.get(0).getLatitude());
            }
            processedList.add(shortPositionRecord);
        }

        //process the raw data, converting the dateTime Instant to milliseconds, rounding to the nearest millisecond
        //i.e. rounding up if fraction of second > 500000 nannoseconds
        for(int i = 1; i < rawDataList.size(); i++) {
            Instant instant = Instant.ofEpochMilli(rawDataList.get(i).getTimeValue().toEpochMilli());
            Duration duration = Duration.between(instant, rawDataList.get(i).getTimeValue());
            if(duration.toNanos() > 500_000) instant = instant.plusMillis(1);
            rawDataList.get(i).setTimeValue(instant);
        }

        /* Now interpolate the position information and populate the processed ArrayList */

        //rawDataPositionCounter - this counter variable controls the position of reading from the
        //raw data ArrayList.  By incrementing the counter based on the last read this saves
        //compute time when searching times in an incremental fashion
        int rawDataPositionCounter = 0;

        //loop through the processed list
        for(int i = 1; i < processedList.size(); i++) {

            //adjust the rawDataPosition counter for each loop
            if(rawDataPositionCounter > 0) rawDataPositionCounter--;

            //loop through the raw data until an Instant is found that is equal to or > the re-sampled time point

            for(int j = rawDataPositionCounter; j < rawDataList.size(); j++) {
                int compareInstants = rawDataList.get(j).getTimeValue().compareTo(processedList.get(i).getTimeValue());
                rawDataPositionCounter = j;

                //if times are equal set lat / long positions and break loop
                if(compareInstants == 0) {
                    processedList.get(i).setLongitude(rawDataList.get(j).getLongitude());
                    processedList.get(i).setLatitude(rawDataList.get(j).getLatitude());
                    break;
                }

                //if raw data Instant > processed then perform linear interpolation of lat / long positions to re-sampled time point
                if(compareInstants > 0) {
                    long timeGap = rawDataList.get(j).getTimeValue().toEpochMilli() - processedList.get(i-1).getTimeValue().toEpochMilli();
                    double longitudeStep = (rawDataList.get(j).getLongitude() - processedList.get(i-1).getLongitude()) / timeGap;
                    double latitudeStep = (rawDataList.get(j).getLatitude() - processedList.get(i-1).getLatitude()) / timeGap;
                    processedList.get(i).setLongitude(processedList.get(i-1).getLongitude() + (longitudeStep * (sampleRate/1_000_000L)));
                    processedList.get(i).setLatitude(processedList.get(i-1).getLatitude() + (latitudeStep * (sampleRate/1_000_000L)));
                    break;
                }
            }

        }

        return processedList;
    }
}
