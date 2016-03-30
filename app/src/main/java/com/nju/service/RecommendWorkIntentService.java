package com.nju.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nju.db.db.service.RecommendDbService;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class RecommendWorkIntentService extends IntentService {
    private static final String TAG = RecommendWorkIntentService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RecommendWorkIntentService(){
        super(TAG);
    }
    public RecommendWorkIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<RecommendWork> recommendWorks = intent.getParcelableArrayListExtra(Constant.RECOMMEND);
        if (recommendWorks != null) {
            for (RecommendWork recommendWork:recommendWorks){
                Log.i(TAG, recommendWork.getId() + ""+recommendWork.getContent());
            }
            new RecommendDbService(getBaseContext()).save(recommendWorks);
        }
    }
}
