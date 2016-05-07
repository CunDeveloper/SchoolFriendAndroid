package com.nju.db.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nju.db.MajorAskEntity;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.AlumniQuestion;
import com.nju.util.Constant;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cun on 2016/3/29.
 */
public class MajorAskDbService {
    private static final String TAG = MajorAskDbService.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private SQLiteDatabase db;
    private Context mContext;

    public MajorAskDbService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
        mContext = context;
    }

    public synchronized void save(ArrayList<AlumniQuestion> alumniQuestions) {
        db.execSQL(Constant.DELETE_FROM + MajorAskEntity.TABLE_NAME);
        ContentValues contentValues;
        for (AlumniQuestion alumniQuestion : alumniQuestions) {
            contentValues = new ContentValues();
            contentValues.put(MajorAskEntity.ID, alumniQuestion.getId());
            contentValues.put(MajorAskEntity.LABEL, alumniQuestion.getLabel());
            final String json = gson.toJson(alumniQuestion);
            contentValues.put(MajorAskEntity.CONTENT, json);
            String entryYear = alumniQuestion.getAuthor().getLabel().split(" ")[2];
            HashMap<String, String> degrees = DegreeUtil.degrees(mContext);
            String degree = degrees.get(entryYear);
            Log.i(TAG, degree);
            if (degree != null && !degree.trim().equals("")) {
                contentValues.put(MajorAskEntity.DEGREE, degree);
                db.replace(MajorAskEntity.TABLE_NAME, null, contentValues);
            }
        }
    }

    public ArrayList<AlumniQuestion> getMajorAsks(String degree) {
        ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
        String[] projection = {
                MajorAskEntity.CONTENT
        };
        Cursor cursor;
        if (degree != null && !degree.trim().equals("")) {
            if (degree.equals(Constant.ALL)) {
                cursor = db.query(
                        MajorAskEntity.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            } else {
                String selection = MajorAskEntity.DEGREE + "=?";
                String[] selectionArgs = new String[1];
                selectionArgs[0] = degree;
                cursor = db.query(
                        MajorAskEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
            }

            int contentId = cursor.getColumnIndex(MajorAskEntity.CONTENT);
            AlumniQuestion alumniQuestion;
            while (cursor.moveToNext()) {
                alumniQuestion = (AlumniQuestion) gson.fromJson(cursor.getString(contentId), AlumniQuestion.class);
                alumniQuestions.add(alumniQuestion);
            }
            cursor.close();
        }
        return alumniQuestions;
    }

    public ArrayList<AlumniQuestion> getMajorAsksByDegreeAndLabel(String degree, String label) {
        ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
        String[] projection = {
                MajorAskEntity.CONTENT
        };
        Cursor cursor;
        if (label.equals("")) {
            return getMajorAsks(degree);
        } else if (degree != null && !degree.trim().equals("") && !label.equals("")) {
            if (degree.equals(Constant.ALL)) {
                String selection = MajorAskEntity.LABEL + "=?";
                Log.i(TAG, selection);
                String[] selectionArgs = {label};
                cursor = db.query(
                        MajorAskEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
            } else {
                String selection = MajorAskEntity.DEGREE + "=? AND " + MajorAskEntity.LABEL + "=?";
                Log.i(TAG, selection);
                String[] selectionArgs = {degree, label};
                cursor = db.query(
                        MajorAskEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
            }

            int contentId = cursor.getColumnIndex(MajorAskEntity.CONTENT);
            AlumniQuestion alumniQuestion;
            while (cursor.moveToNext()) {
                alumniQuestion = (AlumniQuestion) gson.fromJson(cursor.getString(contentId), AlumniQuestion.class);
                alumniQuestions.add(alumniQuestion);
            }
            cursor.close();
        }
        return alumniQuestions;
    }

    public ArrayList<AlumniQuestion> getMajorAsks() {
        ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
        String[] projection = {
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
        AlumniQuestion alumniQuestion;
        while (cursor.moveToNext()) {
            alumniQuestion = (AlumniQuestion) gson.fromJson(cursor.getString(contentId), AlumniQuestion.class);
            Log.i(TAG, alumniQuestion.getDescription());
            alumniQuestions.add(alumniQuestion);
        }
        cursor.close();
        return alumniQuestions;
    }
}
