package com.innoble.stocknbarrel.service;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheldon Hall on 11/19/2016.
 */
@JsonObject
public class NearbyStoresNotification {

    @JsonField
    public int counter = 0;

    @JsonField
    public String previousDate = "";

    @JsonField
    public List<Notification> notifications = null;

    public  NearbyStoresNotification(){
        notifications = new ArrayList<Notification>();
    }
}


@JsonObject
class Notification{
    @JsonField
    String storeLogoPath = "";

    @JsonField
    String discount = "";

    @JsonField
    String storeName = "";

    @JsonField
    String branch_name = "";

    @JsonField
    String numberOfAds = "";

    @JsonField
    int branchId = 0;
}