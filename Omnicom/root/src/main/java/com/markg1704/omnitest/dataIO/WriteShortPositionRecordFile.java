package com.markg1704.omnitest.dataIO;

import com.markg1704.omnitest.config.OmniTestConfig;
import com.markg1704.omnitest.model.HeaderRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;

import java.io.*;
import java.util.List;

/*
Class WriteShortPositionRecordFile

Methods;
public static void writeFile(List<ShortPositionRecord> recordList, String fileName) throws IOException

Method to write out a ShortPositionRecord .csv file.

Method inputs String - filename to write, List<ShortPositionRecords> - the data to write

Output file contains four columns separated by a comma;

Longitude, Latitude, DateTime (Instant), DeltaTime in milliseconds

 */

public class WriteShortPositionRecordFile {

    public static void writeFile(List<ShortPositionRecord> recordList, String fileName) throws IOException {

        String fileString = OmniTestConfig.getOmnitestSystemPath() + fileName;

        try (PrintWriter pr = new PrintWriter(new File(fileString))){

            recordList.forEach((r) -> pr.println(r.getLongitude() + "," + r.getLatitude() + "," + r.getTimeValue() +
                    "," + (r.getTimeValue().toEpochMilli() - HeaderRecord.getTimeZero().toEpochMilli())));

        }



    }
}
