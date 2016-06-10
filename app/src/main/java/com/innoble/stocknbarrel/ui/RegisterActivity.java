package com.innoble.stocknbarrel.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.utils.TrackedApplication;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.Callback {

    private StockNBarrelDatabaseHelper db;
    private Tracker mTracker;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the shared Analytics Tracker instance.
        TrackedApplication application = (TrackedApplication) getApplication();
        mTracker = application.getDefaultTracker();

        this.db =  new StockNBarrelDatabaseHelper(this);
        setContentView(R.layout.activity_register);

    }


    @Override
    protected void onResume() {
        super.onResume();
        name = "Register Screen";
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /*
        TODO: Update save user to DB
     */
    @Override
    public void onUserRegister(Map<String, String> data) {
        if(data == null) return;
        //Do something with the data once it has been returned ( Persist data to local db)
        final String name = data.get("name");
        final String email = data.get("email");
        final double budget = Double.parseDouble(data.get("budget"));


        AsyncTask<Void,Void,Boolean> userRegisterTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return db.addUser(name,email,budget);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(!result) throw new IllegalStateException("Could not initialize user");

                else {

                    mTracker.set("&uid", email );

                    // This hit will be sent with the User ID value and be visible in
                    // User-ID-enabled views (profiles).
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("User")
                            .setAction("Login")
                            .setLabel(email)
                            .build());

                    Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(iMain);
                    finish();
                }
            }
        };

        userRegisterTask.execute();

    }


}
