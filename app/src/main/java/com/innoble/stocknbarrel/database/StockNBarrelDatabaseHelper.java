package com.innoble.stocknbarrel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innoble.stocknbarrel.model.DataEntity;
import com.innoble.stocknbarrel.model.Grocery;
import com.innoble.stocknbarrel.model.GroceryStockItem;
import com.innoble.stocknbarrel.model.Product;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;
import com.innoble.stocknbarrel.model.User;

import java.io.File;

/**
 * Created by At3r on 5/17/2016.
 */
public class StockNBarrelDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_PATH = "/data/data/com.innoble.stocknbarrel/databases/";
    public static final String DATABASE_NAME = "stocknbarrel.sqlite";

    public StockNBarrelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //db will be created when constructor is called
    }

    private boolean databaseExist()
    {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void seedData(SQLiteDatabase db){

        //create user
        User user = new User("Random Shopper", "testuser@gmail.com", 2000.0f);
        insertData(db, user);


        Grocery grocery = new Grocery("Massy Stores", "Chaguanas", "Chaguanas");
        insertData(db, grocery);

        Grocery grocery2 = new Grocery("Extra Foods ", "Grand Bazzar", "Grand Bazzar");
        insertData(db, grocery2);

        ShoppingList shoppingList = new ShoppingList("Monthly Grocery List", user.getId());
        insertData(db, shoppingList);

        //db = this.getWritableDatabase();
        Product product = new Product("Planters Unsalted Mixed Nuts");
        insertData(db, product);
        GroceryStockItem groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 65.99, "tin", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 70.99, "tin", 1000);
        insertData(db, groceryStockItem);

        product.setName("Axe (Excite) Body Spray");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 25.99, "can", 1000);
        insertData(db, groceryStockItem);

        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 30.99, "can", 1000);
        insertData(db, groceryStockItem);

        ShoppingListItem  shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Panadol Hot Rem");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 19.99, "box", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 25.99, "can", 1000);
        insertData(db, groceryStockItem);


        product.setName("Gullon Sugar Free Vanilla Wafer");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),6, 15.99, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 20.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Irish Spring Body Wash (Original)");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 26.00, "bottle", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 31.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Protox Insecticide Spray");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 16.00, "can", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 21.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Airwick Airfreshener 4in1");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 23.00, "can", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 28.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Kiss Whole Grain Loaf ");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 13.00, "loaf", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 17.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Dole Pineapple Juice Unsweetened");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 9.00, "tin", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 14.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Kiss Cakes Orange Cupcakes");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 3.50, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 8.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Bermudez Wheat Crisps");
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 3.00, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 8.99, "can", 1000);
        insertData(db, groceryStockItem);


        Product.createNameIndex(db);

        // Create a shopping list

        //Create a shopping list items
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            Grocery.onCreate(db);
            User.onCreate(db);
            Product.onCreate(db);
            ShoppingList.onCreate(db);
            GroceryStockItem.onCreate(db);
            ShoppingListItem.onCreate(db);
            seedData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Grocery.onUpgrade(db, oldVersion, newVersion);
        User.onUpgrade(db, oldVersion, newVersion);
        Product.onUpgrade(db, oldVersion, newVersion);
        ShoppingList.onUpgrade(db, oldVersion, newVersion);
        GroceryStockItem.onUpgrade(db, oldVersion, newVersion);
        ShoppingListItem.onUpgrade(db, oldVersion, newVersion);
        seedData(db);
    }

    public boolean insertData(SQLiteDatabase db, DataEntity entity){
        //SQLiteDatabase db = this.getWritableDatabase();
        entity.insert(db);
        return true;
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


    public boolean userExsits( )
    {
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

    public User getUser( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1;", null);
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }else if(cursor.getCount() > 0){
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }


    public Cursor getShoppingList( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sli._id as _id, p.name as product_name, gsi.price as price, sli.quantity as quantity, gsi.unit as unit, vendor.name as vendor_name from " + ShoppingListItem.TABLE_SHOPPING_LIST_ITEM + " as sli "
               +  " inner join " + GroceryStockItem.TABLE_GROCERY_STOCK_ITEM + " as gsi on sli." + ShoppingListItem.COLUMN_GROCERY_STOCK_ITEM_ID + "=gsi." + GroceryStockItem.COLUMN_ID
                + " inner join " + Product.TABLE_PRODUCT + " as p on gsi." + GroceryStockItem.COLUMN_PRODUCT_ID + "=p." + Product.COLUMN_ID
                +" inner join "+Grocery.TABLE_GROCERY+ " as vendor on gsi."+GroceryStockItem.COLUMN_GROCERY_ID + "=vendor."+Grocery.COLUMN_ID+";", null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public boolean addUser(String name, String email, double budget )
    {
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

    public boolean deleteUserById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int result = db.delete("user", "_id=?", new String[]{id + ""});
        return result > 0;

    }

    public User getUserByEmail(String email) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=?;", new String[]{email});
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }else if(cursor.getCount() > 0){
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }
}
