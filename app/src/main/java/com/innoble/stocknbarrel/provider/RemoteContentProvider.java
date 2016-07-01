package com.innoble.stocknbarrel.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.innoble.stocknbarrel.BuildConfig;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kemron on 30/06/2016.
 */
public class RemoteContentProvider extends ContentProvider {

    public static final String PRODUCTS_PATH = "products";
    public static final String SUGGESTION_PATH = "suggestions";
    public static final String GROCERY_STOCK_ITEM_PATH = "grocerystockitems";
    public static final String AUTHORITY = "com.innoble.stocknbarrel.remoteprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String VENDOR_ITEM_PATH = "vendoritems";

    private static final int PRODUCT_SEARCH = 25;
    private static final int VENDOR_ITEMS = 50;


    private StockNBarrelDatabaseHelper db;
    private OkHttpClient httpClient;
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);



    private static Uri REMOTE_PRODUCT_SUGGESTION_URI = Uri.parse(BuildConfig.REMOTE_ADDRESS)
            .buildUpon().appendPath(PRODUCTS_PATH).appendPath(SUGGESTION_PATH).build();

    private static Uri VENDOR_ITEMS_URI = Uri.parse(BuildConfig.REMOTE_ADDRESS)
            .buildUpon().appendPath(VENDOR_ITEM_PATH).build();









    static {
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_PATH + "/search_suggest_query/*", PRODUCT_SEARCH);
        sURIMatcher.addURI(AUTHORITY, GROCERY_STOCK_ITEM_PATH + "/*", VENDOR_ITEMS);
    }






    @Override
    public boolean onCreate() {
        db = new StockNBarrelDatabaseHelper(getContext());
        httpClient = new OkHttpClient();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (sURIMatcher.match(uri)){
            case PRODUCT_SEARCH:
               cursor= getSuggestions(uri.getLastPathSegment());
                break;

            case VENDOR_ITEMS:
                cursor = getVendorItemsByName(uri.getLastPathSegment());
                break;
        }
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





    private Cursor getVendorItemsByName(String name){
        String[] columns = new String[]{
             BaseColumns._ID,
                "product_name",
                "product_short_description",
                "product_long_description",
                "product_thumbnail",
                "grocery_id",
                "grocery_name",
                "grocery_branch",
                "vendor_location",
                "vendor_phone",
                "price",
                "unit",
                "grocery_stock_item_id"
        };


        Uri uri = VENDOR_ITEMS_URI
                .buildUpon()
                .appendPath(name)
                .build();

        Request request = new Request.Builder().url(uri.toString()).build();
        MatrixCursor cursor = new MatrixCursor(columns);
        try {
            Response response = httpClient.newCall(request).execute();
            String jsonString = response.body().string();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject item = jsonArray.getJSONObject(i);
                cursor.addRow(new Object[]{
                        item.getLong("ProductID"),
                        item.getString("Name"),
                        item.getString("LongDescription"),
                        item.getString("ShortDescription"),
                        item.getString("Thumbnail"),
                        item.getString("VendorID"),
                        item.getString("VendorName"),
                        item.getString("VendorBranch"),
                        item.getString("VendorLocation"),
                        item.getString("VendorPhone"),
                        item.getDouble("Price"),
                        item.getString("Unit"),
                        item.getLong("VendorItemID")
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }


    private Cursor getSuggestions(String query){
        Uri uri = REMOTE_PRODUCT_SUGGESTION_URI
                .buildUpon()
                .appendPath(query)
                .build();
        Request request = new Request.Builder().url(uri.toString()).build();
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
                }
        );

        try {
            Response response = httpClient.newCall(request).execute();
            String jsonString = response.body().string();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                long id = item.getLong("_id");
                String name = item.getString("name");
                cursor.addRow( new Object[]{id,name,name});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }






}
