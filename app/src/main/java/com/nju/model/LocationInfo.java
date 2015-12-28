package com.nju.model;

import android.graphics.drawable.Drawable;

/**
 * Created by xiaojuzhang on 2015/11/19.
 */
public class LocationInfo {
    private String locationName;
    private String address;
    private double distance;
    private Drawable selectBg;
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Drawable getSelectBg() {
        return selectBg;
    }

    public void setSelectBg(Drawable selectBg) {
        this.selectBg = selectBg;
    }

}
