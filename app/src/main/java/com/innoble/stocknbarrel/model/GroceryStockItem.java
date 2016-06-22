package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class GroceryStockItem extends DataEntity {
    // Database table
    public static final String TABLE_GROCERY_STOCK_ITEM = "grocery_stock_item";
    public static final String COLUMN_GROCERY_ID = "grocery_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_QUANTITY_IN_STOCK = "quantity";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_GROCERY_STOCK_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROCERY_ID + " integer,"
            + COLUMN_PRODUCT_ID + " integer,"
            + COLUMN_SIZE + " real,"
            + COLUMN_PRICE + " real,"
            + COLUMN_UNIT + " text,"
            + COLUMN_QUANTITY_IN_STOCK + " integer,"
            + " FOREIGN KEY(" + COLUMN_GROCERY_ID + ") REFERENCES " + Grocery.TABLE_GROCERY + "(" + Grocery.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES " + Product.TABLE_PRODUCT + "(" + Product.COLUMN_ID + ")"
            + ");";
    private long groceryId;
    private long productId;
    private double size;
    private double price;
    private String unit;
    private long quantityInStock;

    public GroceryStockItem(long groceryId, long productId, double size, double price, String unit, long quantityInStock) {
        super();

        this.groceryId = groceryId;
        this.productId = productId;
        this.size = size;
        this.price = price;
        this.unit = unit;
        this.quantityInStock = quantityInStock;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_STOCK_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_STOCK_ITEM);
        onCreate(database);
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(long quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getSize() {
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

    public double getPrice() {
        return price;
    }

    // Database creation SQL statement

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROCERY_ID, groceryId);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_PRODUCT_ID, productId);
        contentValues.put(COLUMN_QUANTITY_IN_STOCK, quantityInStock);
        contentValues.put(COLUMN_SIZE, size);
        contentValues.put(COLUMN_UNIT, unit);
        long result = database.insert(TABLE_GROCERY_STOCK_ITEM, null, contentValues);
        setId(result);
    }

}
