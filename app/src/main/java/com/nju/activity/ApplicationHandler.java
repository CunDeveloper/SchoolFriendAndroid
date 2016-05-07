package com.nju.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nju.service.ContentService;
import com.nju.util.Constant;

import java.util.ArrayList;

import model.Content;

/**
 * Created by xiaojuzhang on 2015/12/14.
 */
public class ApplicationHandler extends Handler {

    public static final String TAG = ApplicationHandler.class.getSimpleName();
    private static ApplicationHandler mHandler;

    private ApplicationHandler() {
    }

    public static ApplicationHandler newInstance() {
        if (mHandler == null) {
            mHandler = new ApplicationHandler();
        }
        return mHandler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == Constant.SAVE_CONTENT_MESG) {
            ArrayList<Content> contents = (ArrayList<Content>) msg.obj;
            ContentService contentService = new ContentService();
            contentService.save(contents);
            int[] ids = {51};
            contentService.delete(ids);
            ArrayList<Content> tests = contentService.query();
            for (Content content : tests) {
                Log.e(TAG, content.getId() + " === " + content.getContent());
            }
        }
    }
}
