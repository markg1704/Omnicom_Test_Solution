package com.markg1704.omnitest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.markg1704.omnitest.model.DatabaseSchema.IMAGE_TABLE;
import static com.markg1704.omnitest.model.DatabaseSchema.POSITION_TABLE;

/*
Class DatabaseMaintenance

This class is for general database maintenance execution.

public void clean()

This method deletes all records in the positions and images tables and resets the autoincrement to zero.

 */

@Component
public class DatabaseMaintenance {

    private Connection connection;

    //Sql statements String variable declarations
    private static final String CLEAN_POSITION_TABLE = "DELETE FROM " + POSITION_TABLE;
    private static final String RESET_POSITION_TABLE = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME= '" + POSITION_TABLE + "'";
    private static final String CLEAN_IMAGE_TABLE = "DELETE FROM " + IMAGE_TABLE;
    private static final String RESET_IMAGE_TABLE = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME= '" + IMAGE_TABLE + "'";

    @Autowired
    public DatabaseMaintenance(Connection connection) {
        this.connection = connection;
    }

    public void clean() {

        try {

            System.out.println("Beginning database clean.");

            Connection conn = this.connection;

            //Build and execute statements to delete and reset tables
            Statement deletePositionStatement = conn.createStatement();
            deletePositionStatement.execute(CLEAN_POSITION_TABLE);
            Statement resetPositionTable = conn.createStatement();
            resetPositionTable.execute(RESET_POSITION_TABLE);
            System.out.println("Position table cleaned and reset.");

            Statement deleteImagesStatement = connection.createStatement();
            deleteImagesStatement.execute(CLEAN_IMAGE_TABLE);
            Statement resetImageTable = connection.createStatement();
            resetImageTable.execute(RESET_IMAGE_TABLE);
            System.out.println("Image table cleaned and reset.");

            System.out.println("Database cleaning complete.");

        } catch (SQLException e) {
            System.out.println("Error cleaning database.");
            System.out.println(e.getMessage());
        }

    }

}
