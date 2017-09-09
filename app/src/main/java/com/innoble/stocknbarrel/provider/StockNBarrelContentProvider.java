package com.innoble.stocknbarrel.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.Branch;
import com.innoble.stocknbarrel.model.BranchStockItem;
import com.innoble.stocknbarrel.model.Product;
import com.innoble.stocknbarrel.model.ShoppingCart;
import com.innoble.stocknbarrel.model.ShoppingCartItem;
import com.innoble.stocknbarrel.model.User;

/**
 * Created by At3r on 5/19/2016.`
 */
public class StockNBarrelContentProvider extends ContentProvider {

    public static final String PRODUCTS_PATH = "products";
    public static final String GROCERY_PATH = "groceries";
    public static final String GROCERY_STOCK_ITEM_PATH = "grocerystockitems";
    public static final String USERS_PATH = "users";
    public static final String SHOPPING_LISTS_PATH = "shoppinglists";
    public static final String SHOPPING_LIST_ITEMS_PATH = "shoppinglistitems";
    public static final String AUTHORITY = "com.innoble.stocknbarrel.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final int PRODUCTS = 10;
    private static final int PRODUCT_ID = 20;
    private static final int PRODUCT_SEARCH = 25;
    private static final int GROCERIES = 30;
    private static final int GROCERY_ID = 40;
    private static final int GROCERY_STOCK_ITEMS = 50;
    private static final int GROCERY_STOCK_ITEM_ID = 55;
    private static final int GROCERY_STOCK_ITEM_NAMED_PRODUCT_MATCH = 60;
    private static final int NAMED_GROCERY_STOCK_ITEMS = 65;
    private static final int USERS = 70;
    private static final int USER_ID = 80;
    private static final int SHOPPING_LISTS = 90;
    private static final int SHOPPING_LIST_ID = 100;
    private static final int SHOPPING_LIST_ITEMS = 110;
    private static final int SHOPPING_LIST_ITEM_ID = 120;
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
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH + "/*", NAMED_GROCERY_STOCK_ITEMS);
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH + "/#", GROCERY_STOCK_ITEM_ID);
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH + "/#/*", GROCERY_STOCK_ITEM_NAMED_PRODUCT_MATCH);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LISTS_PATH, SHOPPING_LISTS);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LISTS_PATH + "/#", SHOPPING_LIST_ID);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LIST_ITEMS_PATH, SHOPPING_LIST_ITEMS);
        sURIMatcher.addURI(AUTHORITY, SHOPPING_LIST_ITEMS_PATH + "/#", SHOPPING_LIST_ITEM_ID);
    }

    private StockNBarrelDatabaseHelper database;

    @Override
    public boolean onCreate() {
        database = new StockNBarrelDatabaseHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //checkColumns(projection);

        Cursor cursor = null;

        cursor = executeRawQueryIfAvailable(
                uri, projection, selection, selectionArgs, sortOrder);


        if (cursor == null) {
            DatabaseObject dbObj = new DatabaseObject(uri);
            queryBuilder.setTables(dbObj.tableName);          // Set the table
            if (dbObj.isTable == DatabaseQueryType.ROW) {
                queryBuilder.appendWhere(dbObj.tableIdColumnName + "=" + dbObj.rowId);
            } else if (dbObj.isTable == DatabaseQueryType.SEARCH) {
                projection = dbObj.projection;
                queryBuilder.appendWhere(dbObj.whereClause);
            }

            SQLiteDatabase db = database.getReadableDatabase();

            cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, "10");

            if (cursor != null && cursor.moveToFirst() && dbObj.notificationUri != null) {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }
        }


        // make sure that potential listeners are getting notified

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }


    /**
     * Executes raw queries for specified requests
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    private Cursor executeRawQueryIfAvailable(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SHOPPING_LIST_ITEMS:
                Cursor cursor = database.getShoppingList();
                if (cursor != null) {
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return cursor;

            default:
                return null;
        }
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
        int uriType = sURIMatcher.match(uri);
        int count = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        switch (uriType) {
            case SHOPPING_LIST_ITEM_ID:
                count = ShoppingCartItem.removeById(db, Long.parseLong(uri.getLastPathSegment()));
                break;
            case SHOPPING_LIST_ITEMS:
                db.delete(ShoppingCartItem.TABLE_SHOPPING_CART_ITEM, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);

        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int count = 0;
        Uri notifyUri = null;
        switch (uriType) {
            case SHOPPING_LIST_ITEM_ID:
                count = ShoppingCartItem.updateRow(
                        database.getWritableDatabase(),
                        Long.parseLong(uri.getLastPathSegment()),
                        values);
                if (count > 0) {
                    notifyUri = CONTENT_URI.buildUpon().appendPath(SHOPPING_LIST_ITEMS_PATH).build();
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);

        }
        if (notifyUri != null) {
            //     getContext().getContentResolver().notifyChange(notifyUri,null);
        }

        return count;
    }

    /*
    private void checkColumns(String[] projection, string table) {
        String[] available = { TodoTable.COLUMN_CATEGORY,
                TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_LONG_DESCRIPTION,
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

    public boolean userExsits() {
        return database.userExsits();
    }

    public User getUserByEmail(String email) {
        return database.getUserByEmail(email);
    }

    public User getUser() {
        return database.getUser();
    }

    public Cursor getShoppingList() {
        return database.getShoppingList();
    }

    public boolean addUser(String name, String email, double budget) {
        return database.addUser(name, email, budget);
    }

    public boolean deleteUserById(long _id) {
        return database.deleteUserById(_id);
    }

    enum DatabaseQueryType {
        TABLE, ROW, SEARCH
    }

    private class DatabaseObject {

        String tableName;
        String rowId;
        String tableIdColumnName;
        DatabaseQueryType isTable = DatabaseQueryType.TABLE;
        String whereClause;
        String[] projection;
        Uri notificationUri;

        public DatabaseObject(Uri uri) {
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case PRODUCTS:
                    tableName = Product.TABLE_PRODUCT;
                    break;
                case PRODUCT_ID:
                    tableName = Product.TABLE_PRODUCT;
                    tableIdColumnName = Product.COLUMN_ID;
                    rowId = uri.getLastPathSegment();
                    isTable = DatabaseQueryType.ROW;
                    break;
                case PRODUCT_SEARCH:
                    tableName = Product.TABLE_PRODUCT;
                    String searchData = uri.getLastPathSegment();
                    projection = new String[]{"_id", Product.COLUMN_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1, Product.COLUMN_NAME + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA};
                    whereClause = Product.COLUMN_NAME + " LIKE '%" + searchData + "%'";
                    isTable = DatabaseQueryType.SEARCH;
                    break;

                case SHOPPING_LIST_ITEM_ID:
                    tableName = ShoppingCartItem.TABLE_SHOPPING_CART_ITEM;
                    isTable = DatabaseQueryType.ROW;
                    tableIdColumnName = ShoppingCartItem.COLUMN_ID;
                    rowId = uri.getLastPathSegment();
                    break;

                case SHOPPING_LISTS:
                    tableName = ShoppingCart.TABLE_SHOPPING_CART;
                    break;

                case NAMED_GROCERY_STOCK_ITEMS:
                    StringBuilder builder = new StringBuilder();
                    tableName = builder.append(Branch.TABLE_BRANCH)
                            .append(" INNER JOIN ")
                            .append(BranchStockItem.TABLE_BRANCH_STOCK_ITEM)
                            .append(" ON ")
                            .append(Branch.TABLE_BRANCH + "." + Branch.COLUMN_ID)
                            .append(" = ")
                            .append(BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "." + BranchStockItem.COLUMN_BRANCH_ID)
                            .append(" INNER JOIN ")
                            .append(Product.TABLE_PRODUCT)
                            .append(" ON ")
                            .append(BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "." + BranchStockItem.COLUMN_PRODUCT_ID)
                            .append(" = ")
                            .append(Product.TABLE_PRODUCT + "." + Product.COLUMN_ID)
                            .toString();

                    projection = new String[]{
                            Product.TABLE_PRODUCT + "." + Product.COLUMN_ID + " as _id",
                            Product.TABLE_PRODUCT + "." + Product.COLUMN_NAME + " as product_name",
                            Product.TABLE_PRODUCT + "." + Product.COLUMN_SHORT_DESCRIPTION + " as product_short_description",
                            Product.TABLE_PRODUCT + "." + Product.COLUMN_LONG_DESCRIPTION + " as product_long_description",
                            Product.TABLE_PRODUCT + "." + Product.COLUMN_THUMBNAIL + " as product_thumbnail",
                            Branch.TABLE_BRANCH + "." + Branch.COLUMN_NAME + " as grocery_name",
                            Branch.TABLE_BRANCH + "." + Branch.COLUMN_BRANCH + " as grocery_branch",
                            Branch.TABLE_BRANCH + "." + Branch.COLUMN_ADDRESS + " as vendor_location",
                            Branch.TABLE_BRANCH + "." + Branch.COLUMN_PHONE + " as vendor_phone",
                            BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "." + BranchStockItem.COLUMN_PRICE + " as price",
                            BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "." + BranchStockItem.COLUMN_UNIT + " as unit",
                            BranchStockItem.TABLE_BRANCH_STOCK_ITEM + "." + BranchStockItem.COLUMN_ID + " as grocery_stock_item_id",


                    };

                    isTable = DatabaseQueryType.SEARCH;
                    whereClause = Product.TABLE_PRODUCT + "." + Product.COLUMN_NAME + " LIKE '%" + uri.getPathSegments().get(1) + "%'";
            }
        }


    }

}
