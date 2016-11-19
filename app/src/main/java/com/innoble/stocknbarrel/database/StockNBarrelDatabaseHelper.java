package com.innoble.stocknbarrel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innoble.stocknbarrel.model.Branch;
import com.innoble.stocknbarrel.model.BranchStockItem;
import com.innoble.stocknbarrel.model.DataEntity;
import com.innoble.stocknbarrel.model.Product;
import com.innoble.stocknbarrel.model.Promotion;
import com.innoble.stocknbarrel.model.ShoppingCart;
import com.innoble.stocknbarrel.model.ShoppingCartItem;
import com.innoble.stocknbarrel.model.Store;
import com.innoble.stocknbarrel.model.User;

import java.io.File;
import java.util.Random;

/**
 * Created by At3r on 5/17/2016.
 */
public class StockNBarrelDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_PATH = "/data/data/com.innoble.stocknbarrel/databases/";
    public static final String DATABASE_NAME = "stocknbarrel.sqlite";

    public StockNBarrelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //db will be created when constructor is called
    }

    private boolean databaseExist() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void seedData(SQLiteDatabase db) {

        Random random = new Random(System.currentTimeMillis());
        //create user
        User user = new User("Random Shopper", "testuser@gmail.com", 2000.0f);
        insertData(db, user);
        ShoppingCart shoppingCart = new ShoppingCart("Monthly Branch List", user.getId());
        insertData(db, shoppingCart);
        Product.createNameIndex(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Store.onCreate(db);
        Branch.onCreate(db);
        Promotion.onCreate(db);
        User.onCreate(db);
        Product.onCreate(db);
        ShoppingCart.onCreate(db);
        BranchStockItem.onCreate(db);
        ShoppingCartItem.onCreate(db);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Branch.onUpgrade(db, oldVersion, newVersion);
        User.onUpgrade(db, oldVersion, newVersion);
        Product.onUpgrade(db, oldVersion, newVersion);
        ShoppingCart.onUpgrade(db, oldVersion, newVersion);
        BranchStockItem.onUpgrade(db, oldVersion, newVersion);
        ShoppingCartItem.onUpgrade(db, oldVersion, newVersion);
        seedData(db);
    }

    public boolean insertData(SQLiteDatabase db, DataEntity entity) {
        //SQLiteDatabase db = this.getWritableDatabase();
        entity.insert(db);
        return true;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        //return result;
        return null;
    }

    public boolean updateData(String Id, String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();

        return true;
    }

    /**
     * Checks if there is registered user in database
     *
     * @return true if registered user exists and false otherwise
     */
    public boolean userExsits() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1;", null);
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return false;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        return cursor.getCount() > 0;
    }

    /**
     * Retrieves registered user from database
     *
     * @return returns first registered user in database
     */
    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1;", null);
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        } else if (cursor.getCount() > 0) {
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }


    /**
     * Retrieves user's shopping cart items
     *
     * @return Shopping cart Cursor
     */
    public Cursor getShoppingList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sli._id as _id, p.name as product_name, p.short_description as short_description,  p.long_description as long_description, p.thumbnail as product_thumbnail, gsi.price as price, sli.quantity as quantity, gsi.unit as unit, vendor.name as vendor_name,"
                + " vendor.phone as vendor_phone, vendor.location as vendor_location from " + ShoppingCartItem.TABLE_SHOPPING_CART_ITEM + " as sli "
                + " inner join " + BranchStockItem.TABLE_BRANCH_STOCK_ITEM + " as gsi on sli." + ShoppingCartItem.COLUMN_BRANCH_STOCK_ITEM_ID + "=gsi." + BranchStockItem.COLUMN_ID
                + " inner join " + Product.TABLE_PRODUCT + " as p on gsi." + BranchStockItem.COLUMN_PRODUCT_ID + "=p." + Product.COLUMN_ID
                + " inner join " + Branch.TABLE_BRANCH + " as vendor on gsi." + BranchStockItem.COLUMN_BRANCH_ID + "=vendor." + Branch.COLUMN_ID + ";", null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    /**
     * Adds new user to database
     *
     * @param name   - user's name
     * @param email  - user's email address
     * @param budget - user's budget
     * @return returns true if insertion was successful and false otherwise
     */
    public boolean addUser(String name, String email, double budget) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from user where email=?", new String[]{email});
        if (cursor != null && !cursor.moveToFirst() && cursor.getCount() > 0) {
            return false;
        }

        User user = new User(name, email, budget);
        insertData(db, user);
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("budget", budget);
        db.insert("user", null, contentValues);*/
        // make sure that potential listeners are getting notified
        return true;
    }


    /**
     * Removes user from database
     *
     * @param id - Id of user to be removed
     * @return - Returns true if at least one record was removed
     */
    public boolean deleteUserById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int result = db.delete("user", "_id=?", new String[]{id + ""});
        return result > 0;

    }

    /**
     * Retrieves  user from database given a specific email address
     *
     * @param email - email address of user to be retrieved
     * @return User model containing first matched record. Returns null if not record was found
     */
    public User getUserByEmail(String email) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=?;", new String[]{email});
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        } else if (cursor.getCount() > 0) {
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }


    public Cursor executeQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
