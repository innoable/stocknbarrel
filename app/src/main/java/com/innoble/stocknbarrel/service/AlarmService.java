package com.innoble.stocknbarrel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by At3r on 11/19/2016.
 */
public class AlarmService {

    // Setup a recurring alarm every half hour
    public static void scheduleAlarm(Context context) {

        boolean alarmDoesNotExist = (PendingIntent.getBroadcast(context, NearbyStoreAlarmReceiver.REQUEST_CODE,
                new Intent(context, NearbyStoreAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) == null);
        if (alarmDoesNotExist)
        {
            Log.i("Sheldon", "Creating alarm as it no exist");
            // Construct an intent that will execute the AlarmReceiver
            Intent intent = new Intent(context, NearbyStoreAlarmReceiver.class);
            // Create a PendingIntent to be triggered when the alarm goes off
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, NearbyStoreAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Setup periodic alarm every 5 seconds
            long firstMillis = System.currentTimeMillis(); // alarm is set right away
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
            // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 2*60*1000, pIntent);
        }
        else{
            Log.i("Sheldon", "Alarm already exists, look for log outputs");
        }
    }

}
