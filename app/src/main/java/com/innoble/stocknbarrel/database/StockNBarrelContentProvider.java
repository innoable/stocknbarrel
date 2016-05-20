package com.innoble.stocknbarrel.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by At3r on 5/19/2016.
 */
public class StockNBarrelContentProvider extends ContentProvider {

    private StockNBarrelDatabaseHelper database;

    public static final String STOCK_ITEMS_PATH = "stockitems";
    private static final int  STOCK_ITEMS = 10;
    private static final int STOCK_ITEM_ID = 20;

    public static final String GROCERY_PATH = "groceries";
    private static final int  GROCERIES = 30;
    private static final int GROCERY_ID = 40;

    public static final String GROCERY_STOCK_ITEM_PATH = "grocerystockitems";
    private static final int  GROCERY_STOCK_ITEMS = 50;
    private static final int GROCERY_STOCK_ITEM_ID = 60;

    public static final String USERS_PATH = "users";
    private static final int  USERS = 70;
    private static final int USER_ID = 80;

    public static final String SHOPPING_LISTS_PATH = "shoppinglists";
    private static final int  SHOPPING_LISTS = 90;
    private static final int SHOPPING_LIST_ID = 100;

    public static final String SHOPPING_LIST_ITEMS_PATH = "shoppinglistitems";
    private static final int  SHOPPING_LIST_ITEMS = 110;
    private static final int SHOPPING_LIST_ITEM_ID = 120;


    public static final String AUTHORITY = "com.innoble.stocknbarrel.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, STOCK_ITEMS_PATH, STOCK_ITEMS);
        sURIMatcher.addURI(AUTHORITY, STOCK_ITEMS_PATH + "/#", STOCK_ITEM_ID);
        sURIMatcher.addURI(AUTHORITY, GROCERY_PATH, GROCERIES);
        sURIMatcher.addURI(AUTHORITY, GROCERY_PATH + "/#", GROCERY_ID);
        sURIMatcher.addURI(AUTHORITY, USERS_PATH, USERS);
        sURIMatcher.addURI(AUTHORITY, USERS_PATH + "/#", USER_ID);
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH, GROCERY_STOCK_ITEMS);
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH + "/#", GROCERY_STOCK_ITEM_ID);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LISTS_PATH, SHOPPING_LISTS);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LISTS_PATH + "/#", SHOPPING_LIST_ID);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LIST_ITEMS_PATH, SHOPPING_LIST_ITEMS);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LIST_ITEMS_PATH + "/#", SHOPPING_LIST_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        database = new StockNBarrelDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sURIMatcher.match(uri);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /*
    private void checkColumns(String[] projection, string table) {
        String[] available = { TodoTable.COLUMN_CATEGORY,
                TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION,
                TodoTable.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }*/

}
