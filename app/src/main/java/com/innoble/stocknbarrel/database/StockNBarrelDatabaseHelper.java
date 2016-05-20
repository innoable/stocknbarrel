package com.innoble.stocknbarrel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innoble.stocknbarrel.model.Grocery;
import com.innoble.stocknbarrel.model.GroceryStockItem;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;
import com.innoble.stocknbarrel.model.StockItem;
import com.innoble.stocknbarrel.model.User;

/**
 * Created by At3r on 5/17/2016.
 */
public class StockNBarrelDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stocknbarrel.sqlite";

    public StockNBarrelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //db will be created when constructor is called
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Grocery.onCreate(db);
        User.onCreate(db);
        StockItem.onCreate(db);
        GroceryStockItem.onCreate(db);
        ShoppingList.onCreate(db);
        ShoppingListItem.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Grocery.onUpgrade(db, oldVersion, newVersion);
        User.onUpgrade(db, oldVersion, newVersion);
        StockItem.onUpgrade(db, oldVersion, newVersion);
        GroceryStockItem.onUpgrade(db, oldVersion, newVersion);
        ShoppingList.onUpgrade(db, oldVersion, newVersion);
        ShoppingListItem.onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String name, String surname, String marks){
        SQLiteDatabase db = this.getWritableDatabase();

        return false;
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        //return result;
        return null;
    }

    public boolean  updateData(String Id, String name, String surname, String marks)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return true;
    }



}
