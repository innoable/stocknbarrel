package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by At3r on 7/26/2016.
 */
public class Advertisement extends DataEntity {

    public static final String TABLE_ADVERTISMENT = "advertisement";
    public static final String COLUMN_AD_TITLE = "ad_title";
    public static final String COLUMN_AD_IMAGE_PATH = "ad_image_path";
    public static final String COLUMN_AD_DESCRIPTION = "ad_description";
    public static final String COLUMN_AD_TYPE = "ad_type";
    public static final String COLUMN_AD_POST_DATE = "ad_post_date";
    public static final String COLUMN_AD_EXPIRATION_DATE = "ad_expiration_date";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ADVERTISMENT
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AD_TITLE + " text,"
            + COLUMN_AD_IMAGE_PATH + " text,"
            + COLUMN_AD_DESCRIPTION + " text,"
            + COLUMN_AD_POST_DATE + " text,"
            + COLUMN_AD_EXPIRATION_DATE + " text"
            + ");";

    private String title;
    private String path;
    private String description;
    private Date postedDate;
    private Date expirationDate;


    public Advertisement(String title, String path, String description, Date postedDate, Date expirationDate) {
        super();
        this.title = title;
        this.path = path;
        this.description = description;
        this.postedDate = postedDate;
        this.expirationDate = expirationDate;
    }

    public Advertisement(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTISMENT);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTISMENT);
        onCreate(database);
    }

    public static int removeById(SQLiteDatabase database, long id) {
        return database.delete(TABLE_ADVERTISMENT, "_id=" + id, null);
    }

    public static int updateRow(SQLiteDatabase database, long id, ContentValues content) {
        return database.update(TABLE_ADVERTISMENT, content, DataEntity.COLUMN_ID + "=" + Long.toString(id), null);
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AD_TITLE, title);
        contentValues.put(COLUMN_AD_IMAGE_PATH, path);
        contentValues.put(COLUMN_AD_DESCRIPTION, description);
        contentValues.put(COLUMN_AD_POST_DATE, postedDate.toString());
        contentValues.put(COLUMN_AD_EXPIRATION_DATE, expirationDate.toString());

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_ADVERTISMENT, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_ADVERTISMENT, null, contentValues);
            setId(result);
        }

    }
}
