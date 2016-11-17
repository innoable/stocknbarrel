package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 7/21/2016.
 */
public class Category  extends DataEntity {

    // Database table
    public static final String TABLE_CATEGORY = "category";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text"
            + ");";

    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String name) {
        super();
        this.categoryName = name;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(database);
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, categoryName);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_CATEGORY, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_CATEGORY, null, contentValues);
            setId(result);
        }
    }
}
