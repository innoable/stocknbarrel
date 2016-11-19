package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 7/26/2016.
 */
public class Promotion extends DataEntity {

    public static final String TABLE_PROMOTION = "promotion";
    public static final String COLUMN_BRANCH_ID = "branch_id";
    public static final String COLUMN_ADVERTISEMENT_ID = "advertisement_id";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PROMOTION
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BRANCH_ID + " integer,"
            + COLUMN_ADVERTISEMENT_ID + " integer,"
            + " FOREIGN KEY(" + COLUMN_BRANCH_ID + ") REFERENCES " + Branch.TABLE_BRANCH + "(" + Branch.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_ADVERTISEMENT_ID + ") REFERENCES " + Advertisement.TABLE_ADVERTISMENT + "(" + Advertisement.COLUMN_ID + ")"
            + ");";

    private long advertisementId;
    private long branchId;

    public long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public Promotion(long advertisementId, long branchId) {
        this.advertisementId = advertisementId;
        this.branchId = branchId;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTION);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTION);
        onCreate(database);
    }

    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRANCH_ID, branchId);
        contentValues.put(COLUMN_ADVERTISEMENT_ID, advertisementId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_PROMOTION, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_PROMOTION, null, contentValues);
            setId(result);
        }

    }

}
