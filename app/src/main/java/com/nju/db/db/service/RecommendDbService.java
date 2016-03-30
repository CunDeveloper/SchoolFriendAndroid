package com.nju.db.db.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.nju.db.RecommendEntity;
import com.nju.db.SchoolFriendDbHelper;

import com.nju.model.RecommendWork;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class RecommendDbService {
    private static final String TAG = RecommendDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;

    public RecommendDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(ArrayList<RecommendWork> recommendWorks) {
        db.beginTransaction();
        String sql = "INSERT INTO "+ RecommendEntity.TABLE_NAME+"("+RecommendEntity.ID+" ,"
                +RecommendEntity.CONTENT+")values(?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (RecommendWork  recommendWork:recommendWorks) {
            Log.i(TAG,recommendWork.getId()+"");
            stmt.bindLong(1, recommendWork.getId());
            final String json = gson.toJson(recommendWork);
            stmt.bindString(2, json);
            stmt.execute();
        }
        db.setTransactionSuccessful();
        Log.i(TAG, "done");
        db.endTransaction();
    }

    public ArrayList<RecommendWork> getRecommendWorks(){
        ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
        String[] projection ={
                RecommendEntity.CONTENT
        };
        Cursor cursor = db.query(
                RecommendEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(RecommendEntity.CONTENT);
        RecommendWork recommendWork;
        while (cursor.moveToNext()) {
            recommendWork = (RecommendWork) gson.fromJson(cursor.getString(contentId),RecommendWork.class);
            Log.i(TAG,recommendWork.getContent());
            recommendWorks.add(recommendWork);
        }
        cursor.close();
        return recommendWorks;
    }
}
