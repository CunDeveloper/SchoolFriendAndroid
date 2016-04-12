package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nju.db.MajorAskEntity;
import com.nju.db.RecommendEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.fragment.BaseFragment;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cun on 2016/3/29.
 */
public class RecommendDbService {
    private static final String TAG = RecommendDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;
    private static HashMap<String,String> degrees = null;

    public RecommendDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
        if (degrees == null) {
            degrees = new HashMap<>();
            if (context != null) {
                Set<String> stringSet = context.getSharedPreferences(Constant.SCHOOL_FRIEND_SHARED_PREFERENCE, Context.MODE_PRIVATE).getStringSet(Constant.DEGREES, new HashSet<String>());
                for (String str:stringSet){
                    String[] strs = str.split(";");
                    degrees.put(strs[1],strs[0]);
                }
            }

        }
    }

    public void save(ArrayList<RecommendWork> recommendWorks) {

        db.execSQL(Constant.DELETE_FROM+ RecommendEntity.TABLE_NAME);
        ContentValues contentValues;
        for (RecommendWork recommendWork:recommendWorks){
            contentValues = new ContentValues();
            contentValues.put(RecommendEntity.ID, recommendWork.getId());
            int type = recommendWork.getType();
            Log.i(TAG, "TYPE == " + type);
            contentValues.put(RecommendEntity.TYPE, type);
            String entryYear = recommendWork.getAuthor().getLabel().split(" ")[2];
            String degree = degrees.get(entryYear);
            if (degree != null) {
                Log.i(TAG,"DEGREE == "+degrees.get(degree));
                contentValues.put(RecommendEntity.DEGREE, degrees.get(degree));
                final String json = gson.toJson(recommendWork);
                contentValues.put(RecommendEntity.CONTENT, json);
                long id = db.replace(RecommendEntity.TABLE_NAME,null,contentValues);
                Log.i(TAG,"ID == "+id);
            }
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
        String selection;
        String[] selectionArgs = new String[1];
        String[] selectionArgsTwo = new String[2];
        Cursor cursor;
        if (degree.equals(Constant.ALL)){
           selection = RecommendEntity.TYPE +"=?";
            Log.i(TAG,selection);
            selectionArgs[0] = type;
            cursor = db.query(
                    RecommendEntity.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }else {
            selection = RecommendEntity.DEGREE +"=?" +"AND "+RecommendEntity.TYPE +"=?";
            Log.i(TAG,selection);
            selectionArgsTwo[0] = degree;
            selectionArgsTwo[1] = type;
            cursor = db.query(
                    RecommendEntity.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }

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
