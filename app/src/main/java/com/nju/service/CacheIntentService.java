package com.nju.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nju.db.db.service.AlumniVoiceDbService;
import com.nju.db.db.service.MajorAskDbService;
import com.nju.db.db.service.RecommendDbService;
import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniVoice;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class CacheIntentService extends IntentService {
    private static final String TAG = CacheIntentService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public CacheIntentService(){
        super(TAG);
    }
    public CacheIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String label = intent.getStringExtra(Constant.LABEL);
        if (null != label && ! label.equals("")){
            switch (label){
                case Constant.RECOMMEND:{
                    ArrayList<RecommendWork> recommendWorks = intent.getParcelableArrayListExtra(Constant.RECOMMEND);
                    if (recommendWorks != null) {
                        for (RecommendWork recommendWork:recommendWorks){
                            Log.i(TAG, recommendWork.getId() + ""+recommendWork.getContent());
                        }
                        new RecommendDbService(getBaseContext()).save(recommendWorks);
                    }
                    break;
                }
                case Constant.ALUMNI_VOICE:{
                    ArrayList<AlumniVoice> alumniVoices = intent.getParcelableArrayListExtra(Constant.ALUMNI_VOICE);
                    if (alumniVoices != null) {
                        for (AlumniVoice alumniVoice:alumniVoices){
                            Log.i(TAG, alumniVoice.getId() + ""+alumniVoice.getContent());
                        }
                        new AlumniVoiceDbService(getBaseContext()).save(alumniVoices);
                    }
                    break;
                }

                case Constant.MAJOR_ASK:{
                    ArrayList<AlumniQuestion> alumniQuestions = intent.getParcelableArrayListExtra(Constant.MAJOR_ASK);
                    if (alumniQuestions != null) {
                        for (AlumniQuestion alumniQuestion:alumniQuestions){
                            Log.i(TAG, alumniQuestion.getId() + ""+alumniQuestion.getDescription());
                        }
                        new MajorAskDbService(getBaseContext()).save(alumniQuestions);
                    }
                    break;
                }

            }
        }
    }
}
