package com.markg1704.omnitest.dataIO;

import com.markg1704.omnitest.dao.PositionRecordDao;
import com.markg1704.omnitest.model.HeaderRecord;
import com.markg1704.omnitest.model.PositionRecord;
import com.markg1704.omnitest.utils.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;

/*
Class ReadPositions

Class to read, process and write out PositionRecord objects to the database.

Three methods;

public void readPositionFile(String fileName) throws IOException
Method to read, process and write to database

private static void parseRecordHeader(String inputString) throws NumberFormatException
Method to parse input string to extract and write Tzero to HeaderRecord

private static PositionRecord parsePositionRecord(String[] elements) throws NumberFormatException
Method to parse input string to PositionRecord object.


 */

@Repository
public class ReadPositions {

    private PositionRecordDao positionRecordDao;

    @Autowired
    public ReadPositions(PositionRecordDao positionRecordDao) {
        this.positionRecordDao = positionRecordDao;
    }

    public void readPositionFile(String fileName) throws IOException {

        System.out.println("Initialising Position File Read.");

        File file = new File(fileName); //input file
        boolean eof = false; //boolean to control while loop
        int recordsCount = 0; //number of records read counter

        PositionRecord lastRecord = new PositionRecord();

        double intervalDistance = 0.0;
        double intervalTime = 0.0;

        long intervalNannoseconds = 0L;

        //open file
        try(BufferedReader bfr = new BufferedReader(new FileReader(file))) {

            //loop through the input file
            while(!eof) {

                //try block testing for EOFException
                try {

                    String dataLine = bfr.readLine(); //read line from file

                    if(dataLine == null) break; //break out of loop if null record read

                    //skip first (header) line in file
                    if(recordsCount == 0) {
                            recordsCount++;
                            continue;
                        }

                    //split input string into elements based on comma separator
                    String[] elements = dataLine.split(",");

                    //check that line read is a valid record.  If incorrect number of elements continue to next line of file
                    if(elements.length != 14) {
                            System.out.println("Error, incorrect data format on line: " + (recordsCount+1));
                            recordsCount++;
                            continue;
                        }


                    PositionRecord positionRecord = null;

                    //parse the string array into a PositionRecord
                    try {
                        positionRecord = parsePositionRecord(elements);

                        //if this is the first record read extract Tzero and set HeaderRecord field
                        if(recordsCount == 1) {
                            parseRecordHeader(dataLine);
                            positionRecord.setDateTime(HeaderRecord.getTimeZero().plusNanos(intervalNannoseconds).toString());
                            lastRecord = positionRecord;
                        }

                    } catch (NumberFormatException nfe) {
                        System.out.println("Error, unexpected data format on line: " + (recordsCount+1));
                        recordsCount++;
                        continue;
                    }

                    if(recordsCount > 1) {

                        //compute time from the interval distance interval velocity data read from file
                        //first calculate the interval distance between the two points
                        //distance is in meters
                        intervalDistance = positionRecord.getDistance() - lastRecord.getDistance();

                        //calculate interval time in seconds
                        intervalTime = intervalDistance / positionRecord.getVelocity();

                        //convert the time to nannoseconds and add the interval to get total elapsed time
                        intervalNannoseconds += (long)(intervalTime * 1_000_000_000L);

                        //write the PositionRecord object dateTime field by adding the intervalNannoseconds to tZero
                        positionRecord.setDateTime(HeaderRecord.getTimeZero().plusNanos(intervalNannoseconds).toString()); //LocalDateTime.ofInstant(HeaderRecord.getTimeZero().plusNanos(intervalNannoseconds), ZoneId.systemDefault()).format(dtf));

                        //increment the records for the next calculation
                        lastRecord = positionRecord;

                        }

                        //write position to database
                        positionRecordDao.writePositionRecord(positionRecord);

                        if((recordsCount-1)%5000 == 0 && (recordsCount-1) > 0) {
                            System.out.printf("Processing Position File.  Record number: %12d\n", recordsCount-1 );
                        }

                        recordsCount++;


                    } catch (EOFException e) {
                        System.out.println("EOF encountered.");
                        eof = true;
                    }
                }
            }


        System.out.printf("File successfully read. %12d points read.\n", recordsCount-1);


    }

    private static void parseRecordHeader(String inputString) throws NumberFormatException {
        String[] elements = inputString.split(",");
        double dotTickTime = Double.parseDouble(elements[0]);
        long dotTickTimeLong = (long)dotTickTime;
        HeaderRecord.setTimeZero(ToolKit.convertDotNetTicks(dotTickTimeLong));
        System.out.println("Header time set to : " + HeaderRecord.getTimeZero());
    }

    private static PositionRecord parsePositionRecord(String[] elements) throws NumberFormatException {
        PositionRecord record = new PositionRecord();
        record.setLongitude(Double.parseDouble(elements[1]));
        record.setLatitude(Double.parseDouble(elements[2]));
        record.setDistance(Double.parseDouble(elements[7]));
        record.setVelocity(Double.parseDouble(elements[12]));

        return record;
    }
}
