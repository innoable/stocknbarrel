package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class OrderItem extends DataEntity {

    // Database table
    public static final String TABLE_ORDER_ITEM = "order_item";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_BRANCH_STOCK_ITEM_ID = "branch_stock_item_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_ITEM_NOTE = "note";
    public static final String COLUMN_NON_OPTIONAL_ORDER_ITEM = "optional";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ORDER_ITEM
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ORDER_ID + " integer,"
            + COLUMN_BRANCH_STOCK_ITEM_ID + " integer,"
            + COLUMN_QUANTITY + " integer,"
            + COLUMN_ORDER_ITEM_NOTE + " text,"
            + COLUMN_NON_OPTIONAL_ORDER_ITEM + " integer,"
            + " FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES " + Order.TABLE_ORDER + "(" + Order.COLUMN_ID + "),"
            + " FOREIGN KEY(" + COLUMN_BRANCH_STOCK_ITEM_ID + ") REFERENCES " + BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "(" + BranchStockItem.COLUMN_ID + ")"
            + ");";

    private long orderId;
    private long branchStockItemId;
    private int quantity;
    private String note;
    private boolean optional;



    public OrderItem(long orderId, long branchStockItemId, int quantity, String note) {
        super();
        this.orderId = orderId;
        this.branchStockItemId = branchStockItemId;
        this.quantity = quantity;
        this.note = note;
        this.optional = true;
    }

    public OrderItem(long orderId, long branchStockItemId, int quantity, String note, boolean optional) {
        super();
        this.orderId = orderId;
        this.branchStockItemId = branchStockItemId;
        this.quantity = quantity;
        this.note = note;
        this.optional = optional;
    }

    public long getOrderId() {
        return orderId;
    }

    // Database creation SQL statement

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        onCreate(database);
    }

    public static int removeById(SQLiteDatabase database, long id) {
        return database.delete(TABLE_ORDER_ITEM, "_id=" + id, null);
    }

    public static int updateRow(SQLiteDatabase database, long id, ContentValues content) {
        return database.update(TABLE_ORDER_ITEM, content, DataEntity.COLUMN_ID + "=" + Long.toString(id), null);
    }


    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRANCH_STOCK_ITEM_ID, branchStockItemId);
        contentValues.put(COLUMN_QUANTITY, quantity);
        contentValues.put(COLUMN_ORDER_ID, orderId);
        contentValues.put(COLUMN_ORDER_ID, note);

        if(getId() != 0){
            contentValues.put(COLUMN_ID,getId());
            long result = database.insert(TABLE_ORDER_ITEM, null, contentValues);
        }
        else{
            long result = database.insert(TABLE_ORDER_ITEM, null, contentValues);
            setId(result);
        }
    }
}
