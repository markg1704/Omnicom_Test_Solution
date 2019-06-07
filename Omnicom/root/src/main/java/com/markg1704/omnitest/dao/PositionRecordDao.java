package com.markg1704.omnitest.dao;

import com.markg1704.omnitest.model.PositionRecord;
import com.markg1704.omnitest.model.ShortPositionRecord;

import java.util.List;

/*
Interface PositionRecordDao

Defines methods to be implemented for data access to the images table in the database.

 */

public interface PositionRecordDao {

    public PositionRecord getPositionRecord(int id);
    public List<PositionRecord> getAllPositionRecords();

    public boolean writePositionRecord(PositionRecord positionRecord);

    public List<ShortPositionRecord> getAllShortPositionRecords();
}
