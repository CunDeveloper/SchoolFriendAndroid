package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nju.db.MajorAskEntity;
import com.nju.db.RecommendEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
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
        db.execSQL(Constant.DELETE_FROM+ RecommendEntity.TABLE_NAME);
        ContentValues contentValues;
        for (RecommendWork recommendWork:recommendWorks){
            contentValues = new ContentValues();
            contentValues.put(RecommendEntity.ID,recommendWork.getId());
            contentValues.put(RecommendEntity.TYPE,recommendWork.getType());
            contentValues.put(RecommendEntity.DEGREE,recommendWork.getAuthor().getLabel().split(" ")[2]);
            final String json = gson.toJson(recommendWork);
            contentValues.put(RecommendEntity.CONTENT,json);
            db.replace(RecommendEntity.TABLE_NAME,null,contentValues);
        }
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

    public ArrayList<RecommendWork> getRecommendWorksByDegreeAndType(String degree,String type){
        ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
        String[] projection ={
                RecommendEntity.CONTENT
        };
        String selection = RecommendEntity.DEGREE +"=?" +"AND "+RecommendEntity.TYPE +"=?";
        String[] selectionArgs = {degree,type};
        Cursor cursor = db.query(
                RecommendEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
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

    public ArrayList<RecommendWork> getRecommendWorksByType(String type){
        ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
        String[] projection ={
                RecommendEntity.CONTENT
        };
        String selection = RecommendEntity.TYPE +"= ?";
        String[] selectionArgs = {type};
        Cursor cursor = db.query(
                RecommendEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
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