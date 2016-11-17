package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 7/20/2016.
 */
public class SavedShoppingCart extends DataEntity {
    // Database table
    public static final String TABLE_SAVED_SHOPPING_CART = "saved_shopping_cart";
    public static final String COLUMN_SAVED_SHOPPING_CART_NAME = "saved_cart_name";
    public static final String COLUMN_USER_ID = "user_id";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SAVED_SHOPPING_CART
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_ID + " integer,"
            + COLUMN_SAVED_SHOPPING_CART_NAME + " text,"
            + " FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + User.TABLE_USER + "(" + User.COLUMN_ID + ")"
            + ");";

    public SavedShoppingCart(long userId, String savedCartName) {
        super();
        this.userId = userId;
        this.savedCartName = savedCartName;
    }

    private long userId;
    private String savedCartName;

    public String getSavedCartName() {
        return savedCartName;
    }

    public void setSavedCartName(String savedCartName) {
        this.savedCartName = savedCartName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_SHOPPING_CART);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_SHOPPING_CART);
        onCreate(database);
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SAVED_SHOPPING_CART_NAME, savedCartName);
        contentValues.put(COLUMN_USER_ID, userId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_SAVED_SHOPPING_CART, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_SAVED_SHOPPING_CART, null, contentValues);
            setId(result);
        }

    }
}
