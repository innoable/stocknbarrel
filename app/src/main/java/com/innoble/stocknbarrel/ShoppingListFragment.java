package com.innoble.stocknbarrel;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.innoble.stocknbarrel.database.StockNBarrelContentProvider;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.User;

import static android.R.color.holo_red_light;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends android.support.v4.app.Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,ShoppingListAdapter.ItemBtnClickListener{

    private static final int SHOPPING_LIST_LOADER_ID = 0;
    private Uri shoppingListItemQuery;
    private ShoppingListAdapter cursorAdapter;
    private StockNBarrelDatabaseHelper db;
    private TextView tcView;
    private TextView budgetView;


    private double total = 0;
    private User mUser;
    private double budget;

    Integer[] imageId = {
            R.drawable.ic_action_add,
            R.drawable.ic_action_remove
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListItemQuery = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                .appendPath(StockNBarrelContentProvider.SHOPPING_LIST_ITEMS_PATH)
                .build();
        getContext().deleteDatabase(StockNBarrelDatabaseHelper.DATABASE_NAME);
        this.db = new StockNBarrelDatabaseHelper(getActivity());
        this.mUser = db.getUser();
        this.budget = mUser.getBudget();
    }

    public ShoppingListFragment() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(SHOPPING_LIST_LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        cursorAdapter = new ShoppingListAdapter(getActivity(),null,this);


        View rootView =  inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.shopping_list_view);
        tcView = (TextView)rootView.findViewById(R.id.totalCost);
        budgetView = (TextView)rootView.findViewById(R.id.budget);

        budgetView.setText("$"+Double.toString(budget));

        listView.setAdapter(cursorAdapter);



        return rootView;
    }



    private void updateViewTotal(Cursor cursor){

        if(cursor == null){
            tcView.setText("$"+ Double.toString(0.00));
            return;
        }

        double newTotal  = 0.00;
        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            newTotal+= cursor.getDouble(cursor.getColumnIndex("price"));
        }
        tcView.setText("$"+ Double.toString(newTotal));

        if(newTotal > budget){
            tcView.setTextColor(getResources().getColor(holo_red_light));
        }
        else {
            tcView.setTextColor(getResources().getColor(android.R.color.white));
        }
        cursor.moveToFirst();

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                shoppingListItemQuery,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap cursor information into Adapter
        cursorAdapter.swapCursor(data);
        updateViewTotal(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        //Remove Cursor information into adapter
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onBtnClick(long itemId) {
        Uri uri = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                .appendPath(StockNBarrelContentProvider.SHOPPING_LIST_ITEMS_PATH)
                .appendPath(Long.toString(itemId))
                .build();
        getActivity().getContentResolver().delete(uri,null,null);
        getLoaderManager().restartLoader(SHOPPING_LIST_LOADER_ID, null, this);
    }
}
