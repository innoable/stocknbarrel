package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class ShoppingCartItem extends DataEntity {

    // Database table
    public static final String TABLE_SHOPPING_CART_ITEM = "shopping_cart_item";
    public static final String COLUMN_SHOPPING_CART_ID = "shopping_cart_id";
    public static final String COLUMN_BRANCH_STOCK_ITEM_ID = "branch_stock_item_id";
    public static final String COLUMN_QUANTITY = "quantity";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SHOPPING_CART_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SHOPPING_CART_ID + " integer,"
            + COLUMN_BRANCH_STOCK_ITEM_ID + " integer,"
            + COLUMN_QUANTITY + " integer,"
            + " FOREIGN KEY(" + COLUMN_SHOPPING_CART_ID + ") REFERENCES " + ShoppingCart.TABLE_SHOPPING_CART + "(" + ShoppingCart.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_BRANCH_STOCK_ITEM_ID + ") REFERENCES " + BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "(" + BranchStockItem.COLUMN_ID + ")"
            + ");";
    private long shoppingCartId;
    private long branchStockItemId;
    private int quantity;
    public ShoppingCartItem(long shoppingCartId, long branchStockItemId, int quantity) {
        super();
        this.shoppingCartId = shoppingCartId;
        this.branchStockItemId = branchStockItemId;
        this.quantity = quantity;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART_ITEM);
        onCreate(database);
    }

    public static int removeById(SQLiteDatabase database, long id) {
        return database.delete(TABLE_SHOPPING_CART_ITEM, "_id=" + id, null);
    }

    public static int updateRow(SQLiteDatabase database, long id, ContentValues content) {
        return database.update(TABLE_SHOPPING_CART_ITEM, content, DataEntity.COLUMN_ID + "=" + Long.toString(id), null);
    }

    public long getShoppingCartId() {
        return shoppingCartId;
    }

    // Database creation SQL statement

    public void setShoppingCartId(long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
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
        contentValues.put(COLUMN_SHOPPING_CART_ID, shoppingCartId);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_SHOPPING_CART_ITEM, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_SHOPPING_CART_ITEM, null, contentValues);
            setId(result);
        }

    }
}
