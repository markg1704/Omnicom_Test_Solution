package com.markg1704.omnitest.dao;

import com.markg1704.omnitest.model.ImageRecord;

/*
Interface ImageRecordDao

Defines methods to be implemented for data access to the images table in the database.

To complete the test, only one method is actually required; a write method.

A getImageRecord(int id) method is included for testing.

 */

public interface ImageRecordDao {

    public ImageRecord getImageRecord(int id);

    public boolean writeImageRecord(ImageRecord imageRecord);

}
