package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 7/21/2016.
 */
public class ProductCategory extends DataEntity {

    // Database table
    public static final String TABLE_PRODUCT_CATEGORY = "product_category";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCT_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PRODUCT_ID + " integer,"
            + COLUMN_CATEGORY_ID + " integer,"
            + " FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES " + Product.TABLE_PRODUCT + "(" + Product.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + Category.TABLE_CATEGORY + "(" + Category.COLUMN_ID + ")"
            + ");";

    private long productId;
    private long categoryId;

    public ProductCategory(long productId, long categoryId) {
        super();

        this.productId = productId;
        this.categoryId = categoryId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CATEGORY);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CATEGORY);
        onCreate(database);
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_ID, productId);
        contentValues.put(COLUMN_CATEGORY_ID, categoryId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_PRODUCT_CATEGORY, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_PRODUCT_CATEGORY, null, contentValues);
            setId(result);
        }

    }

}
