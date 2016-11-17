package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class SavedShoppingCartItem extends DataEntity {

    // Database table
    public static final String TABLE_SAVED_SHOPPING_CART_ITEM = "saved_shopping_cart_item";
    public static final String COLUMN_SAVED_SHOPPING_CART_ID = "saved_shopping_cart_id";
    public static final String COLUMN_BRANCH_STOCK_ITEM_ID = "branch_stock_item_id";
    public static final String COLUMN_QUANTITY = "quantity";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SAVED_SHOPPING_CART_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SAVED_SHOPPING_CART_ID + " integer,"
            + COLUMN_BRANCH_STOCK_ITEM_ID + " integer,"
            + COLUMN_QUANTITY + " integer,"
            + " FOREIGN KEY(" + COLUMN_SAVED_SHOPPING_CART_ID + ") REFERENCES " + SavedShoppingCart.TABLE_SAVED_SHOPPING_CART + "(" + SavedShoppingCart.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_BRANCH_STOCK_ITEM_ID + ") REFERENCES " + BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "(" + BranchStockItem.COLUMN_ID + ")"
            + ");";
    private long savedShoppingCartId;
    private long branchStockItemId;
    private int quantity;

    public SavedShoppingCartItem(long savedShoppingCartId, long branchStockItemId, int quantity) {
        super();
        this.savedShoppingCartId = savedShoppingCartId;
        this.branchStockItemId = branchStockItemId;
        this.quantity = quantity;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_SHOPPING_CART_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_SHOPPING_CART_ITEM);
        onCreate(database);
    }

    public static int removeById(SQLiteDatabase database, long id) {
        return database.delete(TABLE_SAVED_SHOPPING_CART_ITEM, "_id=" + id, null);
    }

    public static int updateRow(SQLiteDatabase database, long id, ContentValues content) {
        return database.update(TABLE_SAVED_SHOPPING_CART_ITEM, content, DataEntity.COLUMN_ID + "=" + Long.toString(id), null);
    }

    public long getSavedShoppingCartId() {
        return savedShoppingCartId;
    }

    // Database creation SQL statement

    public void setSavedShoppingCartId(long savedShoppingCartId) {
        this.savedShoppingCartId = savedShoppingCartId;
    }

    public long getBranchStockItemId() {
        return branchStockItemId;
    }

    public void setBranchStockItemId(long branchStockItemId) {
        this.branchStockItemId = branchStockItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRANCH_STOCK_ITEM_ID, branchStockItemId);
        contentValues.put(COLUMN_QUANTITY, quantity);
        contentValues.put(COLUMN_SAVED_SHOPPING_CART_ID, savedShoppingCartId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_SAVED_SHOPPING_CART_ITEM, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_SAVED_SHOPPING_CART_ITEM, null, contentValues);
            setId(result);
        }

    }
}
