package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nju.db.AlumniVoiceEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniVoice;
import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/29.
 */
public class AlumniVoiceDbService {
    private static final String TAG = AlumniVoiceDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;

    public AlumniVoiceDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(ArrayList<AlumniVoice> alumniVoices) {
        db.execSQL(Constant.DELETE_FROM + AlumniVoiceEntity.TABLE_NAME);
        ContentValues contentValues;
        for (AlumniVoice alumniVoice : alumniVoices) {
            contentValues = new ContentValues();
            contentValues.put(AlumniVoiceEntity.ID, alumniVoice.getId());
            final String json = gson.toJson(alumniVoice);
            contentValues.put(AlumniVoiceEntity.CONTENT, json);
            db.replace(AlumniVoiceEntity.TABLE_NAME, null, contentValues);
        }
    }

    public ArrayList<AlumniVoice> getAlumniVoice() {
        ArrayList<AlumniVoice> alumniVoices = new ArrayList<>();
        String[] projection = {
                AlumniVoiceEntity.CONTENT
        };
        Cursor cursor = db.query(
                AlumniVoiceEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int contentId = cursor.getColumnIndex(AlumniVoiceEntity.CONTENT);
        AlumniVoice alumniVoice;
        while (cursor.moveToNext()) {
            alumniVoice = (AlumniVoice) gson.fromJson(cursor.getString(contentId), AlumniVoice.class);
            alumniVoices.add(alumniVoice);
        }
        cursor.close();
        return alumniVoices;
    }
}
