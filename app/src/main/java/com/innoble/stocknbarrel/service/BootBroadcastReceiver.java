package com.innoble.stocknbarrel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by At3r on 11/19/2016.
 */

//This class is registered in the AndroidManifest and it's function is to start the AlarmService
//after the phone boots. This way, the AlarmService would act as a background service and in effect
//always be running unless deliberately stopped
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmService.scheduleAlarm(context);
    }
}