package com.innoble.stocknbarrel;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SearchResultActivity extends AppCompatActivity {

    private Tracker mTracker;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Obtain the shared Analytics Tracker instance.
        TrackedApplication application = (TrackedApplication) getApplication();
        mTracker = application.getDefaultTracker();
        //mTracker.set("&uid", user.getEmail() );

        setContentView(R.layout.activity_searchable);
        TextView txt = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Search Calls activity with String Extra  SearchManager.QUERY when user clicks search button
            String query = intent.getStringExtra(SearchManager.QUERY);
            txt.setText("Searching by: "+ query);


            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Product")
                    .setAction("Action Search")
                    .setLabel(query)
                    .build());

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            /* called when user clicks suggestion item
                Displays android:searchSuggestIntentData variable defined in xml search xml file
                followed by the index of the selected item in the list
            */

            String searchTerm = intent.getDataString();
            txt.setText("Suggestion: "+ searchTerm);

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Product")
                    .setAction("Action View")
                    .setLabel(searchTerm)
                    .build());
        }

    }
}
