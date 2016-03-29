package com.nju.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nju.util.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cun on 2016/3/28.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkStateReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int networkType = intent.getExtras().getInt(ConnectivityManager.EXTRA_NETWORK_TYPE);
        boolean isWiFi = networkType == ConnectivityManager.TYPE_WIFI;
        boolean isMobile = networkType == ConnectivityManager.TYPE_MOBILE;
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
        boolean isConnected = networkInfo.isConnected();

        if (isWiFi) {
            if (isConnected) {
                Log.i(TAG, "Wi-Fi - CONNECTED");
                EventBus.getDefault().post(new NetworkStateChanged(Constant.WIFI, true));
                EventBus.getDefault().post(new NetworkInfoEvent(true));
            } else {
                EventBus.getDefault().post(new NetworkStateChanged(Constant.WIFI,false));
                EventBus.getDefault().post(new NetworkInfoEvent(false));
                Log.i(TAG, "Wi-Fi - DISCONNECTED");
            }
        } else if (isMobile) {
            if (isConnected) {
                EventBus.getDefault().post(new NetworkStateChanged(Constant.MOBILE, true));
                Log.i(TAG, "Mobile - CONNECTED");
            } else {
                EventBus.getDefault().post(new NetworkStateChanged(Constant.MOBILE, false));
                Log.i(TAG, "Mobile - DISCONNECTED");
            }
        } else {
            if (isConnected) {
                EventBus.getDefault().post(new NetworkStateChanged(Constant.OTHER, true));
                Log.i(TAG, networkInfo.getTypeName() + " - CONNECTED");
            } else {
                EventBus.getDefault().post(new NetworkStateChanged(Constant.OTHER, false));
                Log.i(TAG, networkInfo.getTypeName() + " - DISCONNECTED");
            }
        }
    }

}
