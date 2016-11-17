package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class Branch extends DataEntity {
    // Database table
    public static final String TABLE_BRANCH = "branch";
    public static final String COLUMN_BRANCH = "branchName";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PHONE = "phone";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BRANCH
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_BRANCH + " text,"
            + COLUMN_LOCATION + " text,"
            + COLUMN_PHONE + " text"
            + ");";
    private String name;
    private String branchName;
    private String location;
    private String phone;

    public Branch(String name, String branchName, String location) {
        super();

        this.name = name;
        this.branchName = branchName;
        this.location = location;
        this.phone = "";

    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH);
        onCreate(database);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchName() {
        return branchName;
    }

    // Database creation SQL statement

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_BRANCH, branchName);
        contentValues.put(COLUMN_PHONE, phone);
        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_BRANCH, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_BRANCH, null, contentValues);
            setId(result);
        }

    }
}

