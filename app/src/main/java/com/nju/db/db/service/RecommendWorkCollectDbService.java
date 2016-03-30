package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nju.db.RecommendCollectEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.RecommendWork;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class RecommendWorkCollectDbService {
    private static final String TAG = RecommendWorkCollectDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;
    public RecommendWorkCollectDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(RecommendWork recommendWork){
        final int id = recommendWork.getId();
        final String content = gson.toJson(recommendWork);
        ContentValues values = new ContentValues();
        values.put(RecommendCollectEntity.ID,id);
        values.put(RecommendCollectEntity.CONTENT,content);
        db.insert(RecommendCollectEntity.TABLE_NAME,null,values);
    }

    public ArrayList<RecommendWork> getCollects(){
        ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
        String[] projection ={
                RecommendCollectEntity.CONTENT
        };
        Cursor cursor = db.query(
                RecommendCollectEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(RecommendCollectEntity.CONTENT);
        RecommendWork recommendWork;
        while (cursor.moveToNext()) {
             recommendWork = (RecommendWork) gson.fromJson(cursor.getString(contentId),RecommendWork.class);
             recommendWorks.add(recommendWork);
        }
        return recommendWorks;
    }

}
