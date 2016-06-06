package com.innoble.stocknbarrel;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.innoble.stocknbarrel.database.StockNBarrelContentProvider;

public class SearchResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DIRECT_SEARCH_LOADER_ID = 1;
    private static final String GROCERY_ID = "1";
    private Uri PRODUCT_OPTIONS_URI = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
            .appendPath(StockNBarrelContentProvider.GROCERY_STOCK_ITEM_PATH)
            .appendPath(GROCERY_ID).build();

    private Tracker mTracker;
    private Intent thisIntent;

    private AsyncTracker aTracker;
    private ListView resultList;
    private SearchResultListAdapter resultListAdapter;

    private String query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_searchable);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        resultList = (ListView) findViewById(R.id.search_result_list);
        resultListAdapter = new SearchResultListAdapter(this, null);
        resultList.setAdapter(resultListAdapter);
        thisIntent = getIntent();

        SharedPreferences queryPref = getPreferences(Context.MODE_PRIVATE);
        if(thisIntent.getStringExtra(SearchManager.QUERY) !=null){
            query = thisIntent.getStringExtra(SearchManager.QUERY);
            queryPref.edit().putString("query",query).commit();
        }
       else query = queryPref.getString("query","");


        // Obtain the shared Analytics Tracker instance.
        TrackedApplication application = (TrackedApplication) getApplication();
        mTracker = application.getDefaultTracker();
        aTracker = new AsyncTracker(mTracker, "");
        getLoaderManager().initLoader(DIRECT_SEARCH_LOADER_ID, null, this);


    }


    @Override
    protected void onResume() {
        super.onResume();
         if (Intent.ACTION_VIEW.equals(thisIntent.getAction())) {
            String searchTerm = thisIntent.getDataString();
            Uri uri = PRODUCT_OPTIONS_URI.buildUpon()
                    .appendPath(searchTerm)
                    .build();
            Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            Intent detailIntent = new Intent(this,ProductDetailActivity.class);
            packageIntentData(detailIntent,cursor);
            aTracker.trackEvent("Search Results", "Product", "Action View", searchTerm);
             startActivity(detailIntent);
             finish();
        }
        else {
            // Search Calls activity with String Extra  SearchManager.QUERY when user clicks search button
            resultList.setOnItemClickListener(new SearchResultClickListener(this));
             aTracker.trackEvent("Search Results", "Product", "Action Search", query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //Associate searchable configuration with the search View

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchResultActivity.class)));
        searchView.setIconifiedByDefault(true);

        return true;
    }




    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if(id ==DIRECT_SEARCH_LOADER_ID) {
            Uri uri = PRODUCT_OPTIONS_URI.buildUpon()
                    .appendPath(query)
                    .build();
            return new CursorLoader(
                    this,
                    uri,
                    null,
                    null,
                    null,
                    null

            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        resultListAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        resultListAdapter.swapCursor(null );

    }


    private class SearchResultClickListener implements AdapterView.OnItemClickListener{

        private Context context;
        SearchResultClickListener(Context context){
            this.context = context;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cur = resultListAdapter.getCursor();
            Intent detailIntent = new Intent(context,ProductDetailActivity.class);
            packageIntentData(detailIntent,cur);

            startActivity(detailIntent);
        }
    }



    private void packageIntentData(Intent intent, Cursor cur){
        intent.putExtra("product_name",cur.getString(cur.getColumnIndex("product_name")));
        intent.putExtra("price",cur.getDouble(cur.getColumnIndex("price")));
        intent.putExtra("unit",cur.getString(cur.getColumnIndex("unit")));
        intent.putExtra("grocery_name",cur.getString(cur.getColumnIndex("grocery_name")));
        intent.putExtra("grocery_stock_item_id",cur.getLong(cur.getColumnIndex("grocery_stock_item_id")));
    }




}
