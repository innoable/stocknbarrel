package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sheldon Hall on 5/19/2016.
 */
public class ShoppingList extends DataEntity {


    // Database table
    public static final String TABLE_SHOPPING_LIST = "shopping_list";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USER_ID = "user_id";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SHOPPING_LIST
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_ID + " integer,"
            + COLUMN_NAME + " text,"
            + " FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + User.TABLE_USER + "(" + User.COLUMN_ID + ")"
            + ");";
    private long userId;
    private String name;

    public ShoppingList(String name, long userId) {
        super();

        this.name = name;
        this.userId = userId;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        onCreate(database);
    }

    public long getUserId() {
        return userId;
    }

    // Database creation SQL statement

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_USER_ID, userId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_SHOPPING_LIST, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_SHOPPING_LIST, null, contentValues);
            setId(result);
        }

    }

}
