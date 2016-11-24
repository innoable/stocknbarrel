package com.innoble.stocknbarrel.service;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.support.v4.app.NotificationCompat.Builder;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.innoble.stocknbarrel.BuildConfig;
import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.ui.MainActivity;
import com.innoble.stocknbarrel.ui.RegisterActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by At3r on 11/19/2016.
 */
public class NearbyStoreService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//public class NearbyStoreService extends IntentService {
    public static final String PREF_NAME = "COLFIRE AFFINITY";
    public static final String PREF_KEY = "NearbyNotifications";
    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation = null;
    private Builder mBuilder = null;

    public NearbyStoreService() {
        super("Nearby Affinity Store Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = sharedPreferences.getString(PREF_KEY, "");
        NearbyStoresNotification notifications = null;
        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        if (!jsonString.trim().isEmpty()) {
            try {
                notifications = LoganSquare.parse(jsonString, NearbyStoresNotification.class);
                if (notifications.previousDate.equals(today) == false ) {
                    notifications = new NearbyStoresNotification();
                }
            } catch (IOException e) {
                notifications = new NearbyStoresNotification();
                Log.e("NearbyStoreService", "Failed to deserialise 'Notification' from shared preferences");
            }
        } else {
            notifications = new NearbyStoresNotification();
        }
        notifications.counter++;
        notifications.previousDate = today;
        mLastLocation = getCurrentLocation();
        if(mLastLocation != null)
        {
            Location oldPosition = new Location("");
            oldPosition.setLatitude(10.496730772222);
            oldPosition.setLongitude(-61.37622710821168);
            float radius = (float) 50.0; //10 metres
            float[] results = new float[1];
            Location.distanceBetween(oldPosition.getLatitude(), oldPosition.getLongitude(),
                    mLastLocation.getLatitude(), mLastLocation.getLongitude(), results);

            if(results[0] <= radius){
                Log.i("NearbyStoreService","You are in range");
                sendSystemNotification(results[0]);
            }
            else
            {
                Log.i("NearbyStoreService","You are out of range");
            }
        }


        try {
            jsonString = LoganSquare.serialize(notifications);
        } catch (IOException e) {
            jsonString = "";
            Log.e("NearbyStoreService", "Failed to serialise 'Notification' to shared preferences");
        }
        editor.putString(PREF_KEY, jsonString);
        editor.commit();

        Log.i("NearbyStoreService", String.format("Serving ran for %d occurrences", notifications.counter));
    }

    private void sendSystemNotification(float distanceBetween) {

        // Sets an ID for the notification, so it can be updated
        int mId = 19822105;

        if(mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.card_in_use)
                    .setContentTitle("Affinity Notification")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("affinity store nearby")
                    //.setDefaults(NotificationCompat.DEFAULT_ALL);
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE);


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, RegisterActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            //stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            //stackBuilder.addNextIntent(resultIntent);
            /*PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );*/

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
        }
        mBuilder.setContentText(String.format("Affinity store %f metres away", distanceBetween));
        // mId allows you to update the notification later on.
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }

    @Override
    public void onCreate() {

        super.onCreate();

        android.os.Debug.waitForDebugger();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }



    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    //@Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = getCurrentLocation();
    }

    //@Override
    public void onConnectionSuspended(int i) {

    }

    //@Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("NearbyStoreService", "Cannot to Google Services API Failed");
    }

    private Location getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return null;
        }

        return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }
}
