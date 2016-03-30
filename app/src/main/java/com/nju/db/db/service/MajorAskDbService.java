package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nju.db.AlumniVoiceEntity;
import com.nju.db.MajorAskEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniQuestion;
import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class MajorAskDbService {
    private static final String TAG = MajorAskDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;

    public MajorAskDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(ArrayList<AlumniQuestion> alumniQuestions) {
        db.execSQL(Constant.DELETE_FROM+ MajorAskEntity.TABLE_NAME);
        ContentValues contentValues;
        for (AlumniQuestion alumniQuestion:alumniQuestions){
            contentValues = new ContentValues();
            contentValues.put(MajorAskEntity.ID,alumniQuestion.getId());
            final String json = gson.toJson(alumniQuestion);
            contentValues.put(MajorAskEntity.CONTENT,json);
            db.replace(MajorAskEntity.TABLE_NAME,null,contentValues);
        }
    }

    public ArrayList<AlumniQuestion>  getMajorAsks(){
        ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
        String[] projection ={
                MajorAskEntity.CONTENT
        };
        Cursor cursor = db.query(
                MajorAskEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(MajorAskEntity.CONTENT);
        AlumniQuestion  alumniQuestion;
        while (cursor.moveToNext()) {
            alumniQuestion = (AlumniQuestion) gson.fromJson(cursor.getString(contentId),AlumniQuestion.class);
            Log.i(TAG,alumniQuestion.getDescription());
            alumniQuestions.add(alumniQuestion);
        }
        cursor.close();
        return alumniQuestions;
    }
}
