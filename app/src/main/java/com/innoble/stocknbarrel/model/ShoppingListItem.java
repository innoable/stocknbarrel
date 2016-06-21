package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class ShoppingListItem extends DataEntity {

    public ShoppingListItem(long shoppingListId, long groceryStockItemId, int quantity) {
        super();
        this.shoppingListId = shoppingListId;
        this.groceryStockItemId = groceryStockItemId;
        this.quantity = quantity;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public long getGroceryStockItemId() {
        return groceryStockItemId;
    }

    public void setGroceryStockItemId(long groceryStockItemId) {
        this.groceryStockItemId = groceryStockItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private long shoppingListId;
    private long groceryStockItemId;
    private int quantity;


    // Database table
    public static final String TABLE_SHOPPING_LIST_ITEM = "shopping_list_item";
    public static final String COLUMN_SHOPPING_LIST_ID = "shopping_list_id";
    public static final String COLUMN_GROCERY_STOCK_ITEM_ID = "grocery_stock_item_id";
    public static final String COLUMN_QUANTITY = "quantity";

    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_SHOPPING_LIST_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SHOPPING_LIST_ID + " integer,"
            + COLUMN_GROCERY_STOCK_ITEM_ID + " integer,"
            + COLUMN_QUANTITY + " integer,"
            + " FOREIGN KEY(" + COLUMN_SHOPPING_LIST_ID + ") REFERENCES "+ ShoppingList.TABLE_SHOPPING_LIST + "(" + ShoppingList.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_GROCERY_STOCK_ITEM_ID + ") REFERENCES "+ GroceryStockItem.TABLE_GROCERY_STOCK_ITEM + "(" + GroceryStockItem.COLUMN_ID + ")"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST_ITEM);
        onCreate(database);
    }

    @Override
    public void insert (SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROCERY_STOCK_ITEM_ID, groceryStockItemId);
        contentValues.put(COLUMN_QUANTITY, quantity);
        contentValues.put(COLUMN_SHOPPING_LIST_ID, shoppingListId);
        long result = database.insert(TABLE_SHOPPING_LIST_ITEM, null, contentValues);
        database.close();
        setId(result);
    }


   public static int removeById(SQLiteDatabase database,long id){
       return database.delete(TABLE_SHOPPING_LIST_ITEM,"_id="+id,null);
   }

    public static int updateRow(SQLiteDatabase database,long id,ContentValues content){
        return database.update(TABLE_SHOPPING_LIST_ITEM,content,DataEntity.COLUMN_ID+"="+Long.toString(id),null);
    }
}
