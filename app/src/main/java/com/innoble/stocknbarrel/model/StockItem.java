package com.innoble.stocknbarrel.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class StockItem  extends DataEntity {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Database table
    public static final String TABLE_STOCK_ITEM = "stock_item";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";


    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_STOCK_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_DESCRIPTION + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_ITEM);
        onCreate(database);
    }

}
