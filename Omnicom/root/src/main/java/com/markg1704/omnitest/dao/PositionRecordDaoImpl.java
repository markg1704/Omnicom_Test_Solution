package com.markg1704.omnitest.dao;

import com.markg1704.omnitest.model.PositionRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.markg1704.omnitest.model.DatabaseSchema.*;

/*
Class PositionRecordDaoImpl implements PositionRecordDao interface

Class variables;

Connection to database (Autowired)
String variables containing Sql code to select from or write to the positions table in the database.

Includes the method public List<ShortPositionRecord> getAllShortPositionRecords() which reads
selected columns from the database and returns an arraylist of ShortPositionRecords.

 */

@Repository
public class PositionRecordDaoImpl implements PositionRecordDao {

    private Connection connection;

    private final String SELECT_POSITION = "SELECT * FROM " + POSITION_TABLE + " WHERE " + POSITION_ID + " = ?";

    private final String SELECT_ALL = "SELECT * FROM " + POSITION_TABLE;

    private final String INSERT_POSITION = "INSERT INTO " + POSITION_TABLE +
                                            " (" + POSITION_LONGITUDE + ", " + POSITION_LATITUDE + ", " + POSITION_DISTANCE +
                                            ", " + POSITION_VELOCITY + ", " + POSITION_DATETIME + ") VALUES(?,?,?,?,?)";

    private final String SELECT_ALL_SHORTRECORDS = "SELECT " + POSITION_LONGITUDE + ", " + POSITION_LATITUDE +
                                                    ", " + POSITION_DATETIME + " FROM " + POSITION_TABLE;

    @Autowired
    public PositionRecordDaoImpl(Connection connection) {

        this.connection = connection;

    }


    @Override
    public PositionRecord getPositionRecord(int id) {

        try {
            Connection conn = this.connection;
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_POSITION);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            PositionRecord record = new PositionRecord();

            record.setId(resultSet.getInt(1));
            record.setLongitude(resultSet.getDouble(2));
            record.setLatitude(resultSet.getDouble(3));
            record.setDistance(resultSet.getDouble(4));
            record.setVelocity(resultSet.getDouble(5));
            record.setDateTime(resultSet.getString(6));

            return record;

        } catch (SQLException e) {

            System.out.println("Error reading record from Position table. Id number : " + id);
            System.out.println(e.getMessage());

        }

        return null;

    }

    @Override
    public List<PositionRecord> getAllPositionRecords() {

        List<PositionRecord> list = new ArrayList<>();

        try {
            Connection conn = this.connection;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while(resultSet.next()) {
                PositionRecord record = new PositionRecord();

                record.setId(resultSet.getInt(1));
                record.setLongitude(resultSet.getDouble(2));
                record.setLatitude(resultSet.getDouble(3));
                record.setDistance(resultSet.getDouble(4));
                record.setVelocity(resultSet.getDouble(5));
                record.setDateTime(resultSet.getString(6));

                list.add(record);
            }

            return list;

        } catch (SQLException e) {

            System.out.println("Error reading all records from Position table.");
            System.out.println(e.getMessage());

        }

        return null;


    }

    @Override
    public boolean writePositionRecord(PositionRecord positionRecord) {

        try {

            Connection conn = this.connection;
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_POSITION);
            preparedStatement.setDouble(1,positionRecord.getLongitude());
            preparedStatement.setDouble(2, positionRecord.getLatitude());
            preparedStatement.setDouble(3, positionRecord.getDistance());
            preparedStatement.setDouble(4, positionRecord.getVelocity());
            preparedStatement.setString(5, positionRecord.getDateTime());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {

            System.out.println("Error writing record to database.");
            System.out.println(e.getMessage());
        }

        return false;

    }

    @Override
    public List<ShortPositionRecord> getAllShortPositionRecords() {

        List<ShortPositionRecord> list = new ArrayList<>();

        try {
           Connection conn = this.connection;
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(SELECT_ALL_SHORTRECORDS);
           while(resultSet.next()) {
               ShortPositionRecord record = new ShortPositionRecord();
               record.setLongitude(resultSet.getDouble(1));
               record.setLatitude(resultSet.getDouble(2));
               Instant instant = Instant.parse(resultSet.getString(3));
               record.setTimeValue(instant);

               list.add(record);

           }

           return list;


        } catch (SQLException e) {
            System.out.println("Error creating ShortPositionRecord list.");
            System.out.println(e.getMessage());
        }

        return null;
    }
}
