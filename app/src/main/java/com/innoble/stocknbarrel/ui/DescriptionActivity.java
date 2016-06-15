package com.innoble.stocknbarrel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innoble.stocknbarrel.R;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,new DescriptionFragment())
                .commit();

    }


    public static class DescriptionFragment extends android.support.v4.app.Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_description,container,false);

            Intent activityIntent = getActivity().getIntent();
            TextView lDescription = (TextView)root.findViewById(R.id.details_long_description);
            TextView sDescription = (TextView)root.findViewById(R.id.details_short_description);
            TextView itemName = (TextView)root.findViewById(R.id.details_item_name);


            itemName.setText(activityIntent.getStringExtra("PRODUCT_NAME"));
            sDescription.setText(activityIntent.getStringExtra("SHORT_DESCRIPTION"));

            String descriptionText = activityIntent.getStringExtra(Intent.EXTRA_TEXT);
            //By default these links will appear but not
            // respond to user input.  To make them active, you need to
            // call setMovementMethod() on the TextView object.
            lDescription.setMovementMethod(LinkMovementMethod.getInstance());
            lDescription.setText(descriptionText);
            return root;
        }
    }
}