package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nju.db.MajorAskCollectEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniQuestion;
import com.nju.model.MajorAsk;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class MajorAskCollectDbService {
    private static final String TAG = MajorAskCollectDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;
    public MajorAskCollectDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(AlumniQuestion majorAsk){
        final int id = majorAsk.getId();
        final String content = gson.toJson(majorAsk);
        ContentValues values = new ContentValues();
        values.put(MajorAskCollectEntity.ID,id);
        values.put(MajorAskCollectEntity.CONTENT,content);
        db.replace(MajorAskCollectEntity.TABLE_NAME,null,values);
    }

    public ArrayList<AlumniQuestion> getCollects(){
        ArrayList<AlumniQuestion>  majorAsks = new ArrayList<>();
        String[] projection ={
                MajorAskCollectEntity.CONTENT
        };
        Cursor cursor = db.query(
                MajorAskCollectEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(MajorAskCollectEntity.CONTENT);
        AlumniQuestion  majorAsk;
        while (cursor.moveToNext()) {
            majorAsk = (AlumniQuestion) gson.fromJson(cursor.getString(contentId),AlumniQuestion.class);
            majorAsks.add(majorAsk);
        }
        cursor.close();
        return majorAsks;
    }

}
