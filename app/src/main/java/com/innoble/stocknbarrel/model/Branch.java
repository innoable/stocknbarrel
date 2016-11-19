package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by At3r on 5/19/2016.
 */
public class Branch extends DataEntity {
    // Database table
    public static final String TABLE_BRANCH = "branch";
    public static final String COLUMN_BRANCH = "branchName";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FAX = "fax";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BRANCH
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_BRANCH + " text,"
            + COLUMN_ADDRESS + " text,"
            + COLUMN_LATITUDE + " text,"
            + COLUMN_LONGITUDE + " text,"
            + COLUMN_EMAIL + " text,"
            + COLUMN_FAX + " text,"
            + COLUMN_PHONE + " text"

            + ");";

    private String name;
    private String branchName;
    private String address;
    private String latitude;
    private String longitude;
    private String phone;
    private String email;
    private String fax;
    private List<Advertisement> branchAds ;


    public Branch(String name, String branchName, String address) {
        super();

        this.name = name;
        this.branchName = branchName;
        this.address = address;
        this.phone = "";
    }



    public Branch(String name, String branchName, String address, String latitude, String longitude, String phone, String email, String fax) {
        super();

        this.name = name;
        this.branchName = branchName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.phone = phone;
        this.fax = fax;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);
        contentValues.put(COLUMN_BRANCH, branchName);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_FAX, fax);

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

