package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 7/21/2016.
 */
public class ProductPhoto extends DataEntity {

    public static final String TABLE_PRODUCT_PHOTOS = "product_photos";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_PHOTO = "photo";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCT_PHOTOS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PRODUCT_ID + " integer,"
            + COLUMN_PRODUCT_PHOTO + " text,"
            + " FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES " + Product.TABLE_PRODUCT + "(" + Product.COLUMN_ID + ")"
            + ");";


    public ProductPhoto(long productId, String photo) {
        super();
        this.productId = productId;
        this.photo = photo;
    }

    private long productId;
    private String photo;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_PHOTOS);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_PHOTOS);
        onCreate(database);
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_PHOTO, photo);
        contentValues.put(COLUMN_PRODUCT_ID, productId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_PRODUCT_PHOTOS, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_PRODUCT_PHOTOS, null, contentValues);
            setId(result);
        }

    }

}
