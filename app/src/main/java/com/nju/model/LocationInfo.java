package com.nju.model;

import android.graphics.drawable.Drawable;

/**
 * Created by xiaojuzhang on 2015/11/19.
 */
public class LocationInfo {

    private String locatName;
    private String address;
    private double distance;
    private Drawable selectBg;
    public String getLocatName() {
        return locatName;
    }

    public void setLocatName(String locatName) {
        this.locatName = locatName;
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
