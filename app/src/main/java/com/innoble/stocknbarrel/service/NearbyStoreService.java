package com.innoble.stocknbarrel.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by At3r on 11/19/2016.
 */
public class NearbyStoreService extends IntentService {
    public NearbyStoreService() {
        super("Nearby Affinity Store Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("NearbyStoreService", "Service running");
    }
}
