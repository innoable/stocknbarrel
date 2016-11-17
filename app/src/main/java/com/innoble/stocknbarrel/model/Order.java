package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by At3r on 7/20/2016.
 */
public class Order extends DataEntity {
    // Database table
    public static final String TABLE_ORDER = "order";
    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_ORDER_PICKUP_ACTION = "order_pickup_action";
    public static final String COLUMN_ORDER_CREATED_ON = "order_created_on";
    public static final String COLUMN_ORDER_LAST_UPDATED_ON = "order_last_updated_on";
    private enum OrderStatus { PLACED, CANCELED, PREPARED, DELIVERED, PICKEDUP };
    private enum OrderAction { PICKUP, DELIVER };

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ORDER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text collate nocase,"
            + COLUMN_ORDER_STATUS + " integer,"
            + COLUMN_ORDER_PICKUP_ACTION + " integer,"
            + COLUMN_ORDER_CREATED_ON + " text,"
            + COLUMN_ORDER_LAST_UPDATED_ON + " text"
            + ");";


    private Date orderDateCreated;
    private Date orderDateLastUpdated;
    private OrderStatus orderStatus;
    private OrderAction orderPickupAction;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderAction getOrderPickupAction() {
        return orderPickupAction;
    }

    public void setOrderPickupAction(OrderAction orderPickupAction) {
        this.orderPickupAction = orderPickupAction;
    }

    public Date getOrderDateCreated() {
        return orderDateCreated;
    }

    public void setOrderDateCreated(Date orderDateCreated) {
        this.orderDateCreated = orderDateCreated;
    }

    public Date getOrderDateLastUpdated() {
        return orderDateLastUpdated;
    }

    public void setOrderDateLastUpdated(Date orderDateLastUpdated) {
        this.orderDateLastUpdated = orderDateLastUpdated;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(database);
    }


    @Override
    public void insert(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ORDER_STATUS, orderStatus.ordinal());
        contentValues.put(COLUMN_ORDER_PICKUP_ACTION, (int) orderPickupAction.ordinal());
        contentValues.put(COLUMN_ORDER_CREATED_ON, orderDateCreated.toString());
        contentValues.put(COLUMN_ORDER_LAST_UPDATED_ON, orderDateLastUpdated.toString());

        if (getId() != 0) {
            contentValues.put(COLUMN_ID, getId());
            long result = database.insert(TABLE_ORDER, null, contentValues);
        } else {
            long result = database.insert(TABLE_ORDER, null, contentValues);
            setId(result);
        }
    }
}
