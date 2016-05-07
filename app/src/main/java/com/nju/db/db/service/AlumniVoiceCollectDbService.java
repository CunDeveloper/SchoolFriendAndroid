package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nju.db.AlumniVoiceCollectEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniVoice;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class AlumniVoiceCollectDbService {
    private static final String TAG = AlumniVoiceCollectDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;

    public AlumniVoiceCollectDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(AlumniVoice alumniVoice) {
        final int id = alumniVoice.getId();
        final String content = gson.toJson(alumniVoice);
        ContentValues values = new ContentValues();
        values.put(AlumniVoiceCollectEntity.ID, id);
        values.put(AlumniVoiceCollectEntity.CONTENT, content);
        db.replace(AlumniVoiceCollectEntity.TABLE_NAME, null, values);
    }

    public ArrayList<AlumniVoice> getCollects() {
        ArrayList<AlumniVoice> alumniVoices = new ArrayList<>();
        String[] projection = {
                AlumniVoiceCollectEntity.CONTENT
        };
        Cursor cursor = db.query(
                AlumniVoiceCollectEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(AlumniVoiceCollectEntity.CONTENT);
        AlumniVoice alumniVoice;
        while (cursor.moveToNext()) {
            alumniVoice = (AlumniVoice) gson.fromJson(cursor.getString(contentId), AlumniVoice.class);
            alumniVoices.add(alumniVoice);
        }
        cursor.close();
        return alumniVoices;
    }

}
