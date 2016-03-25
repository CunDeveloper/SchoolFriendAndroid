package com.nju.activity;

import android.app.Application;

import com.splunk.mint.Mint;

/**
 * Created by xiaojuzhang on 2016/3/25.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Mint.initAndStartSession(getApplicationContext(), "ba2df536");
    }
}
