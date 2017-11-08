package com.innoble.stocknbarrel.model;

import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Sheldon Hall on 5/19/2016.
 */
public class DataEntity {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    private long _id;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public void insert(SQLiteDatabase database) {

    }

}
