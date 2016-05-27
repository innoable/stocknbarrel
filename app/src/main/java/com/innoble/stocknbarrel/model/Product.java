package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class Product extends DataEntity {

    private String name;
    private String description;

    public  Product(String name)
    {
        this.name = name;
    }
    public  Product()
    {

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Database table
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";


    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCT
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text collate nocase,"
            + COLUMN_DESCRIPTION + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(database);
    }


    @Override
    public void insert (SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        Long result = database.insert(TABLE_PRODUCT, null, contentValues);
        setId(result);
    }

    private static final String PRODUCT_INDEX = "create index product_name_index on product (name collate nocase);";

    public static void createNameIndex(SQLiteDatabase database){
        //create index Test_Text_Value_Index
        //on Test (Text_Value collate nocase);
        database.execSQL(PRODUCT_INDEX);
    }


    /*Example of batch insert

        INSERT INTO 'tablename'
          SELECT 'data1' AS 'column1', 'data2' AS 'column2'
            UNION ALL SELECT 'data1', 'data2'
            UNION ALL SELECT 'data1', 'data2'
            UNION ALL SELECT 'data1', 'data2'

     */
}
