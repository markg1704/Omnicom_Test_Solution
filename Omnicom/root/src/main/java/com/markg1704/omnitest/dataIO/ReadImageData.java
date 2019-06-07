package com.markg1704.omnitest.dataIO;

import com.markg1704.omnitest.dao.ImageRecordDao;
import com.markg1704.omnitest.model.HeaderRecord;
import com.markg1704.omnitest.model.ImageRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/*
Class ReadImageData

This class has two methods;

public void readImageFile(String fileName, List<ShortPositionRecord> processedFile).

This method reads in the image .csv file and processes it, aligning it with the Positions records from the
ArrayList processedFile.

private static ImageRecord parseImageRecord(String[] elements).

This method is passed an array of String elements for parsing into an ImageRecord object.

 */

@Repository
public class ReadImageData {

    private ImageRecordDao imageRecordDao;

    @Autowired
    public ReadImageData(ImageRecordDao imageRecordDao) {
        this.imageRecordDao = imageRecordDao;
    }

    public void readImageFile(String fileName, List<ShortPositionRecord> processedFile) throws IOException {

        System.out.println("Initialising Image file read.");
        //method variables
        boolean eof = false;  // EOF boolean variable to control while loop
        int recordsCount = 0; // counter for number of records processed

        //LocalTime variable, constructed from the Hour, Minute, Second fields in image file
        //This variable remains fixed during processing as the time at the start of the file.
        LocalTime localTimeZero = null;

        //LocalTime constructed from the Hour, Minute, Second fields in image file.
        //This variable increments with each record
        LocalTime currentRecordTime;

        //Variable to store the difference between Tzero & Tcurrent.
        long timeDifference;

        //Time difference converted to an absolute frame reference (i.e. number of frames from Tzero) based on sample rate.
        int absoluteFrameReference;

        //Image sample rate converted to milliseconds
        long sampleRate = HeaderRecord.getImageSampleRate() / 1_000_000;

        // image .csv file is opened in a try-with-resources block allowing automatic closing of the file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            //loop through image .csv file
            while(!eof) {

                try {

                    String dataLine = br.readLine();  //read line of data from file
                    if(dataLine == null) break;  // if data is null break out of loop
                    if(recordsCount == 0) {
                        recordsCount++;
                        continue; //loop past header record
                    }

                    String[] elements = dataLine.split(","); //split input string into elements for processing

                    //check file has read correct input format.  Should be six elements in the array
                    //if not 6 elements then give error message and skip to next record
                    if(elements.length != 6) {
                        System.out.println("Error, incorrect data format on line: " + (recordsCount+1));
                        recordsCount++;
                        continue;
                    }

                    //Parse the String array.  Parsing can throw a runtime NumberFormatException.  This exception
                    //is chosen to be handled and if it is thrown there is a problem with the data record.
                    //If the exception is thrown the loop skips to the next record without processing the bad data.
                    ImageRecord record = null;
                    try {
                        record = parseImageRecord(elements); // parse the string array into an ImageRecord
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error, unexpected data format on line: " + (recordsCount+1));
                        recordsCount++;
                        continue;
                    }

                    //Process the ImageRecords

                    //If this is the first record then set Tzero.
                    if(recordsCount == 1) {
                        localTimeZero = LocalTime.of(record.getHour(), record.getMinute(), record.getSecond());
                    }

                    //Calculate time of the image, derive Delta-Time from Tzero and use this to determine the reference frame
                    //of the Image
                    currentRecordTime = LocalTime.of(record.getHour(), record.getMinute(), record.getSecond());
                    timeDifference = Duration.between(localTimeZero, currentRecordTime).toMillis();
                    timeDifference = timeDifference + (record.getFrame() * sampleRate);
                    absoluteFrameReference = ((int)timeDifference/(int)sampleRate);


                    //Look up interpolated position based on absolute reference frame and populate the Longitude, Latitude and DateTime fields
                    //Use if statement to check ImageRecord is within the interpolated position range
                    if(absoluteFrameReference < processedFile.size()) {
                        record.setLongitude(processedFile.get(absoluteFrameReference).getLongitude());
                        record.setLatitude(processedFile.get(absoluteFrameReference).getLatitude());
                        record.setDateTime(processedFile.get(absoluteFrameReference).getTimeValue().toString());
                    }


                    //write record to database
                    imageRecordDao.writeImageRecord(record);

                    if((recordsCount-1)%2000 == 0 && (recordsCount-1) > 0) {
                        System.out.printf("Processing Image file.  Record Number: %10d\n", recordsCount - 1);
                    }

                    recordsCount++;

                } catch (EOFException e) {
                    eof = true;
                }

            }

            System.out.printf("File successfully read. %12d points processed.\n\n", recordsCount-1);
        }
    }

    private static ImageRecord parseImageRecord(String[] elements) throws NumberFormatException {

        ImageRecord imageRecord = new ImageRecord();

        imageRecord.setOffset(Long.parseLong(elements[0]));
        imageRecord.setLength(Long.parseLong(elements[1]));
        imageRecord.setHour(Integer.parseInt(elements[2]));
        imageRecord.setMinute(Integer.parseInt(elements[3]));
        imageRecord.setSecond(Integer.parseInt(elements[4]));
        imageRecord.setFrame(Integer.parseInt(elements[5]));

        return imageRecord;
    }
}
