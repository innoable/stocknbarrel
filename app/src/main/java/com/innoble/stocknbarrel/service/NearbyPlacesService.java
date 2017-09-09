package com.innoble.stocknbarrel.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldonhall on 9/2/17.
 */

public class NearbyPlacesService extends IntentService {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected PlaceDetectionClient mPlaceDetectionClient;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place Name: '%s'%nLatLng: %s%nAddress: %s%nWebsite: %s%nLikelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getPlace().getLatLng().toString(),
                            placeLikelihood.getPlace().getAddress(),
                            placeLikelihood.getPlace().getWebsiteUri().toString(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    public NearbyPlacesService() {

        super("Nearby Affinity Store Service Using Google Places API");


    }

}
