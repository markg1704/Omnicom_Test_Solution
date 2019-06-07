package com.markg1704.omnitest.dao;

import com.markg1704.omnitest.model.ImageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

import static com.markg1704.omnitest.model.DatabaseSchema.*;

/*
Class ImageRecordDaoImpl implements the ImageRecordDao interface.

Class provides concrete methods to access the images table in the database.

Class variables;

Connection
 */

@Repository
public class ImageRecordDaoImpl implements ImageRecordDao {

    private Connection connection;

    private static final String SELECT_IMAGE = "SELECT * FROM " + IMAGE_TABLE + " WHERE " + IMAGE_ID + " = ?";

    private final String INSERT_IMAGE = "INSERT INTO " + IMAGE_TABLE +
            " (" + IMAGE_OFFSET + ", " + IMAGE_LENGTH + ", " + IMAGE_HOUR +
            ", " + IMAGE_MINUTE + ", " + IMAGE_SECOND + ", " + IMAGE_FRAME +
            ", " + IMAGE_LONGITUDE + ", " + IMAGE_LATITUDE + ", " + IMAGE_DATETIME +
            ") VALUES(?,?,?,?,?,?,?,?,?)";


    @Autowired
    public ImageRecordDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ImageRecord getImageRecord(int id) {

        try {

            Connection conn = this.connection;
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            ImageRecord imageRecord = new ImageRecord();

            imageRecord.setId(resultSet.getInt(1));
            imageRecord.setOffset(resultSet.getLong(2));
            imageRecord.setLength(resultSet.getLong(3));
            imageRecord.setHour(resultSet.getInt(4));
            imageRecord.setMinute(resultSet.getInt(5));
            imageRecord.setSecond(resultSet.getInt(6));
            imageRecord.setFrame(resultSet.getInt(7));
            imageRecord.setLongitude(resultSet.getDouble(8));
            imageRecord.setLatitude(resultSet.getDouble(9));
            imageRecord.setDateTime(resultSet.getString(10));

            return imageRecord;
        } catch (SQLException e) {
            System.out.println("Error reading Image Record from database.  Id number: " + id);
        }

        return null;
    }

    @Override
    public boolean writeImageRecord(ImageRecord imageRecord) {

        try {

            Connection conn = this.connection;
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_IMAGE);
            preparedStatement.setLong(1, imageRecord.getOffset());
            preparedStatement.setLong(2, imageRecord.getLength());
            preparedStatement.setInt(3, imageRecord.getHour());
            preparedStatement.setInt(4, imageRecord.getMinute());
            preparedStatement.setInt(5, imageRecord.getSecond());
            preparedStatement.setInt(6, imageRecord.getFrame());
            if(imageRecord.getLongitude() == 0) {
                preparedStatement.setNull(7, Types.DOUBLE);
            } else {
                preparedStatement.setDouble(7, imageRecord.getLongitude());
            }
            if(imageRecord.getLatitude() == 0) {
                preparedStatement.setNull(8, Types.DOUBLE);
            } else {
                preparedStatement.setDouble(8, imageRecord.getLatitude());
            }
            preparedStatement.setString(9, imageRecord.getDateTime());

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error writing Image Record to database.");
            System.out.println(e.getMessage());
        }
        return false;
    }
}
