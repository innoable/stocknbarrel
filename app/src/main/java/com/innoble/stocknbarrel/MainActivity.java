package com.innoble.stocknbarrel;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements RegisterFragment.Callback {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(false) {
            transaction.add(R.id.container, new RegisterFragment()).commit();
        }
        else{
           transaction.add(R.id.container,new ShoppingListFragment()).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);

        //Associate searchable configuration with the search View

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
       // searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this,SearchResultActivity.class)));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public void onUserRegister(Map<String, String> data) {
        if(data == null) return;
        //Do something with the data once it has been returned ( Persist data to local db)
        String name = data.get("name");
        String email = data.get("email");
        Double budget = Double.parseDouble(data.get("budget"));

        Toast t = Toast.makeText(getApplicationContext(),name+"\n"+email+"\n"+budget,Toast.LENGTH_LONG);
        t.show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_register,new ShoppingListFragment())
                .commit();
    }

}
