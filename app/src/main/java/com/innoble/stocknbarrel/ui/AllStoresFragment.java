package com.innoble.stocknbarrel.ui;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.Task;
import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.User;
import com.innoble.stocknbarrel.service.NearbyPlacesService;
import com.innoble.stocknbarrel.service.NearbyStoreAlarmReceiver;
//import com.innoble.stocknbarrel.service.NearbyStoreService;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllStoresFragment extends Fragment {

    private static final int ALL_STORES_LOADER_ID = 0;
    private AllStoresAdapter cursorAdapter;
    private StockNBarrelDatabaseHelper db;
    private User mUser;


    public AllStoresFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.db = new StockNBarrelDatabaseHelper(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    10045);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Cursor allStoresCursor = db.executeQuery("SELECT store_logo, _id FROM store");
        cursorAdapter = new AllStoresAdapter(getActivity(), allStoresCursor);

        View rootView = inflater.inflate(R.layout.fragment_all_stores, container, false);
        GridView listView = (GridView) rootView.findViewById(R.id.all_stores_view);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        GoogleApiAvailability gApiInstance = GoogleApiAvailability.getInstance();
        Context ctx = getActivity();
        int status = gApiInstance.isGooglePlayServicesAvailable(ctx);
        if(status == ConnectionResult.SUCCESS ) {
            Log.i("NearbyStoreService", "Google Pay Services available");
        }
        else{
            ConnectionResult cr = new ConnectionResult(status);
            Log.i("NearbyStoreService", cr.getErrorMessage());
        }

        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.all_stores_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.stop_nearbystore_search:
                Context ctx = getActivity();
                Intent intent = new Intent(ctx, NearbyStoreAlarmReceiver.class);
                final PendingIntent pIntent = PendingIntent.getBroadcast(ctx, NearbyStoreAlarmReceiver.REQUEST_CODE,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(pIntent);
                pIntent.cancel();
                SharedPreferences sharedPreferences = ctx.getSharedPreferences(NearbyPlacesService.PREF_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().remove(NearbyPlacesService.PREF_KEY);
                sharedPreferences.edit().clear();
                sharedPreferences.edit().commit();
                return true;



            case R.id.get_places:
                Context context = getActivity();
                Intent i = new Intent(context, NearbyPlacesService.class);
                i.putExtra("foo", "bar");
                context.startService(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
