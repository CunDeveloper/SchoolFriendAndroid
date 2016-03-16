package com.nju.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.nju.db.SchoolEntry;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.School;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class SchoolService {
    private static final String TAG = SchoolService.class.getSimpleName();
    private SQLiteDatabase db;
    public SchoolService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }
    public void save(ArrayList<School> schools) {
        db.beginTransaction();
        String sql = "INSERT INTO "+ SchoolEntry.TABLE_NAME+"("+SchoolEntry.COLUMN_NAME_SC_ID+" ,"
                +SchoolEntry.COLUMN_NAME_NAME+" ,"+SchoolEntry.COLUMN_NAME_COLLEGE_ID+")"+"values(?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (School school:schools) {
            stmt.bindLong(1,school.getScId());
            stmt.bindString(2, school.getName());
            stmt.bindLong(3, school.getCollegeID());
            stmt.execute();
        }
        db.setTransactionSuccessful();
        Log.i(TAG,"done");
    }

    public ArrayList<School> query() {
        ArrayList schools = new ArrayList();
        String[] projection = {
                SchoolEntry.COLUMN_NAME_SC_ID,
                SchoolEntry.COLUMN_NAME_NAME,
                SchoolEntry.COLUMN_NAME_COLLEGE_ID
        };
        Cursor cursor = db.query(
                SchoolEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int id = cursor.getColumnIndex(SchoolEntry.COLUMN_NAME_SC_ID);
        int nameIndex = cursor.getColumnIndex(SchoolEntry.COLUMN_NAME_NAME);
        int provinceIndex = cursor.getColumnIndex(SchoolEntry.COLUMN_NAME_COLLEGE_ID);
        School school = null;
        while (cursor.moveToNext()) {
            school = new School();
            school.setScId(cursor.getInt(id));
            school.setName(cursor.getString(nameIndex));
            school.setCollegeID(cursor.getInt(provinceIndex));
            schools.add(school);
        }
        return schools;
    }
}
