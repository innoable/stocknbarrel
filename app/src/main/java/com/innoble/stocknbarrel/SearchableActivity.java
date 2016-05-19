package com.innoble.stocknbarrel;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        TextView txt = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Search Calls activity with String Extra  SearchManager.QUERY when user clicks search button
            String query = intent.getStringExtra(SearchManager.QUERY);
            txt.setText("Searching by: "+ query);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            /* called when user clicks suggestion item
                Displays android:searchSuggestIntentData variable defined in xml search xml file
                followed by the index of the selected item in the list
            */
            String uri = intent.getDataString();
            txt.setText("Suggestion: "+ uri);
        }

    }
}
