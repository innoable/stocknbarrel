package com.innoble.stocknbarrel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.Callback {

    private StockNBarrelDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db =  new StockNBarrelDatabaseHelper(this);
        setContentView(R.layout.activity_register);

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
                    Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(iMain);
                    finish();
                }
            }
        };

        userRegisterTask.execute();

    }


}
