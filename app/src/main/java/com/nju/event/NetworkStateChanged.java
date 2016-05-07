package com.nju.event;

/**
 * Created by cun on 2016/3/28.
 */
public class NetworkStateChanged {
    private String netType;
    private boolean mIsInternetConnected;

    public NetworkStateChanged(String netType, boolean isInternetConnected) {
        this.netType = netType;
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }

    public String getNetType() {
        return netType;
    }
}
