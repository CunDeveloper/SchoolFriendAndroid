package com.nju.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.nju.db.CollegeEntry;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.College;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class CollegeService {
    private static final String TAG = CollegeService.class.getSimpleName();

    private SQLiteDatabase db;
    public CollegeService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }
    public void save(ArrayList<College> colleges) {
        db.beginTransaction();
        String sql = "INSERT INTO "+ CollegeEntry.TABLE_NAME+"("+CollegeEntry.COLUMN_NAME_CO_ID+" ,"
                +CollegeEntry.COLUMN_NAME_NAME+" ,"+CollegeEntry.COLUMN_NAME_PROVINCE_ID+")"+"values(?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (College college:colleges) {
            stmt.bindLong(1,college.getCoId());
            stmt.bindString(2, college.getName());
            stmt.bindLong(3, college.getProvinceID());
            stmt.execute();
        }
        db.setTransactionSuccessful();
        Log.i(TAG,"done");
    }

    public ArrayList<College> query() {
        ArrayList colleges = new ArrayList();
        String[] projection = {
                CollegeEntry.COLUMN_NAME_CO_ID,
                CollegeEntry.COLUMN_NAME_NAME,
                CollegeEntry.COLUMN_NAME_PROVINCE_ID
        };
        Cursor cursor = db.query(
                CollegeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int id = cursor.getColumnIndex(CollegeEntry.COLUMN_NAME_CO_ID);
        int nameIndex = cursor.getColumnIndex(CollegeEntry.COLUMN_NAME_NAME);
        int provinceIndex = cursor.getColumnIndex(CollegeEntry.COLUMN_NAME_PROVINCE_ID);
        College college ;
        while (cursor.moveToNext()) {
            college = new College();
            college.setCoId(cursor.getInt(id));
            college.setName(cursor.getString(nameIndex));
            college.setProvinceID(cursor.getInt(provinceIndex));
            colleges.add(college);
        }
        cursor.close();
        return colleges;
    }
}
