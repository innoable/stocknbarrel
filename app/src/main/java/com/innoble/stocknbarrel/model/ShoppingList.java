package com.innoble.stocknbarrel.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class ShoppingList extends DataEntity {


    private int userId;
    private String name;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Database table
    public static final String TABLE_SHOPPING_LIST = "shopping_list";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USER_ID = "user_id";

    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_SHOPPING_LIST
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_ID + " integer,"
            + COLUMN_NAME + " text,"
            + " FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES "+ User.TABLE_USER + "(" + User.COLUMN_ID + ")"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        onCreate(database);
    }

}