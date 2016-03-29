package com.nju.activity;

/**
 * Created by cun on 2016/3/29.
 */
public class NetworkInfoEvent {
    private boolean isConnected;

    public NetworkInfoEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
