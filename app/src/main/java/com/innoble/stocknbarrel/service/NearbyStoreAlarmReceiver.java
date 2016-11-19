package com.innoble.stocknbarrel.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by At3r on 11/19/2016.
 */
public class NearbyStoreAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.innoble.stocknbarrel.action.NearbyAffinityStoreAlarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NearbyStoreService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
