package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by At3r on 5/19/2016.
 */
public class BranchStockItem extends DataEntity {
    // Database table
    public static final String TABLE_BRANCH_STOCK_ITEM = "branch_stock_item";
    public static final String COLUMN_BRANCH_ID = "branch_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_UNIT = "unit";

    public static final String COLUMN_EXPIRATION_DATE = "expiration_date";

    public static final String COLUMN_QUANTITY_IN_STOCK = "quantity";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BRANCH_STOCK_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BRANCH_ID + " integer,"
            + COLUMN_PRODUCT_ID + " integer,"
            + COLUMN_SIZE + " real,"
            + COLUMN_PRICE + " real,"
            + COLUMN_UNIT + " text,"
            + COLUMN_EXPIRATION_DATE + " text,"
            + COLUMN_QUANTITY_IN_STOCK + " integer,"
            + " FOREIGN KEY(" + COLUMN_BRANCH_ID + ") REFERENCES " + Branch.TABLE_BRANCH + "(" + Branch.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES " + Product.TABLE_PRODUCT + "(" + Product.COLUMN_ID + ")"
            + ");";
    private long branchId;
    private long productId;
    private double size;
    private double price;
    private String unit;
    private long quantityInStock;
    private Date expiryDate;

    public BranchStockItem(long branchId, long productId, double size, double price, String unit, long quantityInStock, Date expiryDate) {
        super();

        this.branchId = branchId;
        this.productId = productId;
        this.size = size;
        this.price = price;
        this.unit = unit;
        this.quantityInStock = quantityInStock;
        this.expiryDate = expiryDate;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH_STOCK_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH_STOCK_ITEM);
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

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRANCH_ID, branchId);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_PRODUCT_ID, productId);
        contentValues.put(COLUMN_QUANTITY_IN_STOCK, quantityInStock);
        contentValues.put(COLUMN_SIZE, size);
        contentValues.put(COLUMN_UNIT, unit);
        contentValues.put(COLUMN_EXPIRATION_DATE, expiryDate.toString());

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_BRANCH_STOCK_ITEM, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_BRANCH_STOCK_ITEM, null, contentValues);
            setId(result);
        }

    }

}
