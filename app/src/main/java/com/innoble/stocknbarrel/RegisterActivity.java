package com.innoble.stocknbarrel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    /*
        TODO: Update save user to DB
     */
    @Override
    public void onUserRegister(Map<String, String> data) {
        if(data == null) return;
        //Do something with the data once it has been returned ( Persist data to local db)
        String name = data.get("name");
        String email = data.get("email");
        Double budget = Double.parseDouble(data.get("budget"));

        Toast t = Toast.makeText(getApplicationContext(),name+"\n"+email+"\n"+budget,Toast.LENGTH_LONG);
        t.show();
        Intent iMain = new Intent(this,MainActivity.class);
        startActivity(iMain);
        finish();
    }


}
