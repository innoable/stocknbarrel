package com.innoble.stocknbarrel;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.User;

public class MainActivity extends AppCompatActivity {

    private StockNBarrelDatabaseHelper mDb;
    private Tracker mTracker;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Obtain the shared Analytics Tracker instance.
        TrackedApplication application = (TrackedApplication) getApplication();
        mTracker = application.getDefaultTracker();


        mDb = new StockNBarrelDatabaseHelper(this);
        User user = mDb.getUser();
        if(user == null){
            Intent iLogin = new Intent(this,RegisterActivity.class);
            startActivity(iLogin);
            // to prevent back button from navigating back here from login screen
            finish();
        }

        mTracker.set("&uid", user.getEmail() );
        setContentView(R.layout.activity_main);
        // Set toolbar view as ActionBar in Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,new ShoppingListFragment())
                .addToBackStack("shopping_list")
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        name = "Main Screen";
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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

    /*
            TODO: Return true if user is registered and false otherwise
     */
    private boolean isRegistered(){
            return true;
    }



}
