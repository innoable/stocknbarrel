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
//import android.location.LocationListener;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.support.v4.app.NotificationCompat.Builder;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.innoble.stocknbarrel.BuildConfig;
import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.ui.MainActivity;
import com.innoble.stocknbarrel.ui.RegisterActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sheldon Hall on 11/19/2016.
 */
public class NearbyStoreService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String PREF_NAME = "AFFINITY";
    public static final String PREF_KEY = "NearbyNotifications";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation = null;
    private Builder mBuilder = null;
    private boolean mRequestingLocationUpdates;


    public class UpdateHandler implements Runnable, LocationListener {

        private LocationRequest mLocationRequest = null;
        private GoogleApiClient mGoogleApiClient = null;
        private Context mContext = null;
        private Location knownLocation = null;

        public Location getKnownLocation() {
            return knownLocation;
        }

        public void setKnownLocation(Location knownLocation) {
            this.knownLocation = knownLocation;
        }



        public UpdateHandler(GoogleApiClient mGoogleApiClient, Context mContext) {
            this.mGoogleApiClient = mGoogleApiClient;
            this.mContext = mContext;
            createLocationRequest();
        }

        protected void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(2000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        protected void startLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            Log.i("NearbyStoreService", "Requested location updates");
        }

        @Override
        public void onLocationChanged(Location location) {
            knownLocation = location;
        }

        @Override
        public void run() {
            Looper.prepare();
            Log.d("NearbyStoreService", "inside location thread");

            startLocationUpdates();
            Log.d("NearbyStoreService", "run of location thread ending");
            Looper.loop(); //  <--- this was the fix
        }


        public void removeLocationUpdates() {
            Log.d("NearbyStoreService", "Removing location updates");
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    public NearbyStoreService() {
        super("Nearby Reward Store Service");
    }

    protected void onHandleIntent(Intent intent) {
        Log.i("NearbyStoreService", "Secondary thread proceeding to wait");

        boolean connected = mGoogleApiClient.isConnected();

        try {
            int count = 0;
            while ( connected == false && count < 5) {
                Thread.sleep(1000);
                count++;
                connected = mGoogleApiClient.isConnected();
            }
        } catch (InterruptedException e) {
            Log.d("NearbyStoreService", "Interrupted exception while waiting for api to connect");
        }

        if(connected)
        {
            UpdateHandler update_handler = new UpdateHandler(mGoogleApiClient, this);
            Thread update_handler_thread = new Thread(update_handler);
            try {
                Log.d("NearbyStoreService", "starting location thread");
                update_handler_thread.start();
                int count = 0;
                mLastLocation = update_handler.getKnownLocation();

                while (mLastLocation == null && count < 5) {
                    Thread.sleep(1000);
                    count++;
                    mLastLocation = update_handler.getKnownLocation();
                }
            } catch (InterruptedException e) {
                Log.d("NearbyStoreService", "Interrupted exception while trying to get location in next thread");
                update_handler_thread.interrupt();
            }


            if(mLastLocation != null) doWork();
            update_handler.removeLocationUpdates();
            update_handler_thread.interrupt();
        }
    }


    private void doWork(){
        // Do the task here
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = sharedPreferences.getString(PREF_KEY, "");
        NearbyStoresNotification notifications = null;
        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        if (!jsonString.trim().isEmpty()) {
            try {
                notifications = LoganSquare.parse(jsonString, NearbyStoresNotification.class);
                if (notifications.previousDate.equals(today) == false) {
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

        if (mLastLocation != null) {
            Location oldPosition = new Location("");
            oldPosition.setLatitude(10.496730772222);
            oldPosition.setLongitude(-61.37622710821168);
            float radius = (float) 50.0; //10 metres
            float[] results = new float[1];
            Location.distanceBetween(oldPosition.getLatitude(), oldPosition.getLongitude(),
                    mLastLocation.getLatitude(), mLastLocation.getLongitude(), results);

            if (results[0] <= radius) {
                Log.i("NearbyStoreService", "You are in range");
                sendSystemNotification(results[0]);
            } else {
                Log.i("NearbyStoreService", "You are out of range");
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

        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.card_in_use)
                    .setContentTitle("Affinity Notification")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("affinity store nearby")
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE);


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, RegisterActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);


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

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null && playServicesAvailable()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }


    @Override
    public void onStart(Intent intent, int startId) {
        mGoogleApiClient.connect();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("NearbyStoreService", "Destroying Service");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    //@Override
    public void onConnected(Bundle connectionHint) {
        Log.i("NearbyStoreService", "Connected to Google Services API");
    }

    //@Override
    public void onConnectionSuspended(int i) {
        Log.i("NearbyStoreService", "Connection to Google Services API Suspended");
    }

    //@Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("NearbyStoreService", "Cannot to Google Services API Failed");
    }



    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean playServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Log.i("NearbyStoreService", String.format("PS Package: %s |  PS Version: %d", GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE));
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.i("NearbyStoreService", String.format("Error finding service. Result Code: %d", resultCode));
            } else {
                Log.i("NearbyStoreService", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

}