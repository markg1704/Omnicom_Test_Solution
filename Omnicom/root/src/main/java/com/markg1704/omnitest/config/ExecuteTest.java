package com.markg1704.omnitest.config;

import com.markg1704.omnitest.dao.ImageRecordDao;
import com.markg1704.omnitest.dao.PositionRecordDao;
import com.markg1704.omnitest.dataIO.ReadImageData;
import com.markg1704.omnitest.dataIO.ReadPositions;
import com.markg1704.omnitest.dataIO.WriteShortPositionRecordFile;
import com.markg1704.omnitest.model.HeaderRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;
import com.markg1704.omnitest.utils.TimeAnalysisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/*
Class ExecuteTest

Class with method for executing the program.  Contains one method public void start() and
declared as with Spring @Component annotation for automatic instantiation.

public void start()

Declared with @EventListener(ContextRefreshedEvent.class) annotation.  Code is executed immediately after the Main method
initialises the Spring Context.

The method executes the reading and processing of the positions and images files.

 */

@Component
public class ExecuteTest {

    private static final String positionFileName = "position_records.csv";
    private static final String imageFileName = "image_records.csv";

    private ReadPositions readPositions;
    private ReadImageData readImageData;

    private PositionRecordDao positionRecordDao;
    private ImageRecordDao imageRecordDao;

    private DatabaseMaintenance databaseMaintenance;


    @Autowired
    public ExecuteTest(ReadPositions readPositions, ReadImageData readImageData,
                       PositionRecordDao positionRecordDao,
                       DatabaseMaintenance databaseMaintenance,
                       ImageRecordDao imageRecordDao) {

        System.out.println("Initalising Execution.");
        this.readPositions = readPositions;
        this.readImageData = readImageData;
        this.positionRecordDao = positionRecordDao;
        this.databaseMaintenance = databaseMaintenance;
        this.imageRecordDao = imageRecordDao;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {

        Instant executionStart = Instant.now();
        System.out.println("\nBeginning execution.");

        HeaderRecord.setImageSampleRate(40_000_000L);  //setting sample rate

        databaseMaintenance.clean(); //cleaning database


        String positionFile = OmniTestConfig.getOmnitestSystemPath() + positionFileName; //setting path to position file

        //execute reading and processing of positions file
        try {
            readPositions.readPositionFile(positionFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //retrieve all positions in ShortPositionRecord format
        List<ShortPositionRecord> rawRecordList = positionRecordDao.getAllShortPositionRecords();

        //process raw position records into a new ArrayList of positions interpolated to image sampling points
        List<ShortPositionRecord> processedData = TimeAnalysisTools.interpolatePositionRecords(rawRecordList);

        //write the processed position information to file
        //these are useful for testing position interpolation is correct
        try {
            WriteShortPositionRecordFile.writeFile(rawRecordList, "rawrecords.csv");
            WriteShortPositionRecordFile.writeFile(processedData, "processedrecords.csv");

        } catch (IOException e) {
            System.out.println("Error writing short record file.");
            System.out.println(e.getMessage());
        }

        String imageFile = OmniTestConfig.getOmnitestSystemPath() + imageFileName;  //set path to image file

        //execute reading and processing of the image file.
        //pass the processed position ArrayList to the method
        try {
            readImageData.readImageFile(imageFile, processedData);
        } catch (IOException e) {
            System.out.println("Error reading Image file.");
            System.out.println(e.getMessage());
        }

        Instant executionEnd = Instant.now();
        long elapsedTimeMilliseconds = Duration.between(executionStart, executionEnd).toMillis();
        double elapsedTimeSeconds = (double)elapsedTimeMilliseconds / 1000.0;

        System.out.println("\nExecution completed.");
        System.out.printf("Execution elapsed time: %10.3f seconds", elapsedTimeSeconds);

    }
}
