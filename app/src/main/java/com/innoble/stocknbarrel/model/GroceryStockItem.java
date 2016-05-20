package com.innoble.stocknbarrel.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class GroceryStockItem extends DataEntity {
    private long groceryId;
    private float size;
    private float price;
    private String unit;
    private long quantityInStock;

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public long getGroceryId() {
        return groceryId;
    }

    public void setGroceryId(long groceryId) {
        this.groceryId = groceryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    // Database table
    public static final String  TABLE_GROCERY_STOCK_ITEM = "grocery_stock_item";
    public static final String COLUMN_GROCERY_ID = "grocery_id";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_PRICE= "price";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_QUANTITY_IN_STOCK = "quantity";

    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_GROCERY_STOCK_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROCERY_ID + " integer,"
            + COLUMN_SIZE  + " real,"
            + COLUMN_PRICE  + " real,"
            + COLUMN_UNIT  + " text,"
            + COLUMN_QUANTITY_IN_STOCK + " integer,"
            + " FOREIGN KEY(" + COLUMN_GROCERY_ID + ") REFERENCES "+ Grocery.TABLE_GROCERY + "(" + Grocery.COLUMN_ID + ")"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_STOCK_ITEM);
        onCreate(database);
    }

}
