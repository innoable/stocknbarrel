package com.innoble.stocknbarrel.database;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.innoble.stocknbarrel.model.Product;
import com.innoble.stocknbarrel.model.User;

/**
 * Created by At3r on 5/19/2016.
 */
public class StockNBarrelContentProvider extends ContentProvider {

    private StockNBarrelDatabaseHelper database;

    public static final String PRODUCTS_PATH = "products";
    private static final int  PRODUCTS = 10;
    private static final int PRODUCT_ID = 20;
    private static final int PRODUCT_SEARCH = 25;

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
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_PATH, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_PATH + "/#", PRODUCT_ID);
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_PATH + "/search_suggest_query/*", PRODUCT_SEARCH);
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
        database =  new StockNBarrelDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //checkColumns(projection);

        DatabaseObject dbObj = new DatabaseObject(uri);
        queryBuilder.setTables(dbObj.tableName);          // Set the table
        if(dbObj.isTable == DatabaseQueryType.ROW ) {
            queryBuilder.appendWhere(dbObj.tableIdColumnName + "=" + dbObj.rowId );
        }
        else if(dbObj.isTable == DatabaseQueryType.SEARCH){
            projection = dbObj.projection;
            queryBuilder.appendWhere(dbObj.whereClause);
        }

        SQLiteDatabase db = database.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, "10");
        // make sure that potential listeners are getting notified

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        //cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
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

    enum DatabaseQueryType {
        TABLE, ROW, SEARCH
    }


    public boolean userExsits( )
    {
        return database.userExsits();
    }

    public User getUserByEmail( String email)
    {
        return database.getUserByEmail(email);
    }

    public User getUser( )
    {
        return database.getUser();
    }


    public Cursor getShoppingList( )
    {
        return database.getShoppingList();
    }

    public boolean addUser( String name, String email, double budget )
    {
        return database.addUser(name, email, budget);
    }

    public boolean deleteUserById(long _id)
    {
        return database.deleteUserById(_id);
    }


    private class DatabaseObject {

        String tableName;
        String rowId;
        String tableIdColumnName;
        DatabaseQueryType isTable = DatabaseQueryType.TABLE;
        String whereClause;
        String[] projection;

        public DatabaseObject(Uri uri) {
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case PRODUCTS:
                    tableName = Product.TABLE_PRODUCT;
                    break;
                case PRODUCT_ID:
                    tableName = Product.TABLE_PRODUCT;
                    tableIdColumnName =  Product.COLUMN_ID;
                    rowId = uri.getLastPathSegment();
                    isTable = DatabaseQueryType.ROW;
                    break;
                case PRODUCT_SEARCH:
                    tableName = Product.TABLE_PRODUCT;
                    String searchData = uri.getLastPathSegment();
                    projection = new String[]{ "_id", Product.COLUMN_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1, Product.COLUMN_NAME + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA};
                    whereClause = Product.COLUMN_NAME + " LIKE '%" + searchData + "%'";
                    isTable = DatabaseQueryType.SEARCH;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }


    }

}
