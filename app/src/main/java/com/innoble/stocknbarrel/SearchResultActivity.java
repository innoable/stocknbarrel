package com.innoble.stocknbarrel;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private ListView resultList;
    private SearchResultListAdapter resultListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_searchable);

        resultList = (ListView)findViewById(R.id.search_result_list);
        resultListAdapter = new SearchResultListAdapter(this,null);
        resultList.setAdapter(resultListAdapter);
        thisIntent = getIntent();



        // Obtain the shared Analytics Tracker instance.
        TrackedApplication application = (TrackedApplication) getApplication();
        mTracker = application.getDefaultTracker();

        if (Intent.ACTION_SEARCH.equals(thisIntent.getAction())) {
            // Search Calls activity with String Extra  SearchManager.QUERY when user clicks search button
            getLoaderManager().initLoader(DIRECT_SEARCH_LOADER_ID,null,this);
            resultList.setOnItemClickListener(new SearchResultClickListener(this));



            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Product")
                    .setAction("Action Search")
                    .setLabel(thisIntent.getStringExtra(SearchManager.QUERY))
                    .build());

        } else if (Intent.ACTION_VIEW.equals(thisIntent.getAction())) {
            /* called when user clicks suggestion item
                Displays android:searchSuggestIntentData variable defined in xml search xml file
                followed by the index of the selected item in the list
            */

            String searchTerm = thisIntent.getDataString();

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Product")
                    .setAction("Action View")
                    .setLabel(searchTerm)
                    .build());
        }

    }




    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if(id ==DIRECT_SEARCH_LOADER_ID) {
            String query = thisIntent.getStringExtra(SearchManager.QUERY);
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
            detailIntent.putExtra("product_name",cur.getString(cur.getColumnIndex("product_name")));
            detailIntent.putExtra("price",cur.getDouble(cur.getColumnIndex("price")));
            detailIntent.putExtra("unit",cur.getString(cur.getColumnIndex("unit")));
            detailIntent.putExtra("grocery_name",cur.getString(cur.getColumnIndex("grocery_name")));
            detailIntent.putExtra("grocery_stock_item_id",cur.getLong(cur.getColumnIndex("grocery_stock_item_id")));

            startActivity(detailIntent);
        }
    }


}
