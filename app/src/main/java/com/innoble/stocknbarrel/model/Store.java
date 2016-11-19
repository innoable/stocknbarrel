package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by At3r on 7/20/2016.
 */
public class Store  extends DataEntity {
    // Database table
    public static final String TABLE_STORE = "store";
    public static final String COLUMN_STORE_NAME = "store_name";
    public static final String COLUMN_STORE_DESCRIPTION = "store_description";
    public static final String COLUMN_STORE_LOGO = "store_logo";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_STORE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_STORE_NAME + " text, "
            + COLUMN_STORE_DESCRIPTION + " text, "
            + COLUMN_STORE_LOGO + " text"
            + ");";

    private String storeName;
    private String storeDescription;
    private String logoUri;
    private List<Branch> branches;

    public Store(String storeName, String storeDescription) {
        super();

        this.storeName = storeName;
        this.storeDescription = storeDescription;
    }

    public Store() {

    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        onCreate(database);
    }

    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STORE_NAME, storeName);
        contentValues.put(COLUMN_STORE_DESCRIPTION, storeDescription);
        contentValues.put(COLUMN_STORE_LOGO, logoUri);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_STORE, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_STORE, null, contentValues);
            setId(result);
        }

    }
}
