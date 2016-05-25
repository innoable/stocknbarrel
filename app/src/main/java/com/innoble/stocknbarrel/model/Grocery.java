package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class Grocery extends DataEntity {
    private String name;
    private String branch;
    private String location;

    public Grocery(String name, String branch, String location) {
        super();

        this.name = name;
        this.branch = branch;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    // Database table
    public static final String TABLE_GROCERY = "grocery";
    public static final String COLUMN_BRANCH = "branch";
    public static final String COLUMN_LOCATION = "description";

    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_GROCERY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_BRANCH + " text,"
            + COLUMN_LOCATION
            + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY);
        onCreate(database);
    }

    @Override
    public void insert (SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_BRANCH, branch);
        long result = database.insert(TABLE_GROCERY, null, contentValues);
        setId(result);
    }
}

