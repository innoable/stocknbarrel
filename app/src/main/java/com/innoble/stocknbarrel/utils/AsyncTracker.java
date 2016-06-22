package com.innoble.stocknbarrel.utils;

import android.os.AsyncTask;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by At3r on 5/27/2016.
 */
public class AsyncTracker {

    private Tracker mTracker;
    private String userId;

    public AsyncTracker(Tracker mTracker, String userId) {
        this.mTracker = mTracker;
        this.userId = userId;
    }

    public void trackEvent(final String screen, final String category, final String action, final String label) {
        AsyncTask<Void, Void, Boolean> trackerTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {

                mTracker.setScreenName(screen);
                mTracker.set("&uid", userId);

                // This hit will be sent with the User ID value and be visible in
                // User-ID-enabled views (profiles).
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .build());

                return true;
            }


        };

        trackerTask.execute();
    }
}
