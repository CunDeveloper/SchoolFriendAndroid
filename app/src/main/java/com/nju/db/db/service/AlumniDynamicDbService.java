package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nju.db.AlumniDynamicEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniTalk;
import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cun on 2016/3/29.
 */
public class AlumniDynamicDbService {
    private static final String TAG = AlumniDynamicDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;
    private Context mContext;

    public AlumniDynamicDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
        mContext = context;
    }

    public void save(ArrayList<AlumniTalk> alumniVoices) {
        db.execSQL(Constant.DELETE_FROM + AlumniDynamicEntity.TABLE_NAME);
        ContentValues contentValues;
        for (AlumniTalk alumniVoice : alumniVoices) {
            contentValues = new ContentValues();
            contentValues.put(AlumniDynamicEntity.ID, alumniVoice.getId());
            final String json = gson.toJson(alumniVoice);
            contentValues.put(AlumniDynamicEntity.CONTENT, json);
            String entryYear = alumniVoice.getAuthorInfo().getLabel().split(" ")[2];
            HashMap<String, String> degrees = DegreeUtil.degrees(mContext);
            String degree = degrees.get(entryYear);
            Log.i(TAG, degree);
            if (degree != null && !degree.trim().equals("")) {
                contentValues.put(AlumniDynamicEntity.DEGREE, degree);
                db.replace(AlumniDynamicEntity.TABLE_NAME, null, contentValues);
            }

        }
    }

    public ArrayList<AlumniTalk> getAlumniDynamics(String degree) {
        ArrayList<AlumniTalk> alumniVoices = new ArrayList<>();
        String[] projection = {
                AlumniDynamicEntity.CONTENT
        };
        Cursor cursor;
        if (degree != null && !degree.trim().equals("")) {
            if (degree.equals(Constant.ALL)) {
                cursor = db.query(
                        AlumniDynamicEntity.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            } else {
                String selection = AlumniDynamicEntity.DEGREE + "=?";
                String[] selectionArgs = new String[1];
                selectionArgs[0] = degree;
                cursor = db.query(
                        AlumniDynamicEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
            }

            int contentId = cursor.getColumnIndex(AlumniDynamicEntity.CONTENT);
            AlumniTalk alumniVoice;
            while (cursor.moveToNext()) {
                alumniVoice = (AlumniTalk) gson.fromJson(cursor.getString(contentId), AlumniTalk.class);
                alumniVoices.add(alumniVoice);
            }
            cursor.close();
        }
        return alumniVoices;
    }
}
