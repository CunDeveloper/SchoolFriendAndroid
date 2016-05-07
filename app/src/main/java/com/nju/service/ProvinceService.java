package com.nju.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.nju.db.ProvinceEntry;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.model.Province;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class ProvinceService {
    private static final String TAG = ProvinceService.class.getSimpleName();
    private SQLiteDatabase db;

    public ProvinceService(Context context) {
        db = SchoolFriendDbHelper.newInstance(context).getWritableDatabase();
    }

    public void save(ArrayList<Province> provinces) {
        db.beginTransaction();
        String sql = "INSERT INTO " + ProvinceEntry.TABLE_NAME + "(" + ProvinceEntry.COLUMN_NAME_P_ID + " ,"
                + ProvinceEntry.COLUMN_NAME_P_NAME + " ," + ProvinceEntry.COLUMN_NAME_PROVINCE_ID + ")" + "values(?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (Province province : provinces) {
            stmt.bindLong(1, province.getPid());
            stmt.bindString(2, province.getName());
            stmt.bindLong(3, province.getProvinceID());
            stmt.execute();
        }
        db.setTransactionSuccessful();
        Log.i(TAG, "done");
    }

    public ArrayList<Province> query() {
        ArrayList provinces = new ArrayList();
        String[] projection = {
                ProvinceEntry.COLUMN_NAME_P_ID,
                ProvinceEntry.COLUMN_NAME_P_NAME,
                ProvinceEntry.COLUMN_NAME_PROVINCE_ID
        };
        Cursor cursor = db.query(
                ProvinceEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int id = cursor.getColumnIndex(ProvinceEntry.COLUMN_NAME_P_ID);
        int nameIndex = cursor.getColumnIndex(ProvinceEntry.COLUMN_NAME_P_NAME);
        int provinceIndex = cursor.getColumnIndex(ProvinceEntry.COLUMN_NAME_PROVINCE_ID);
        Province province = null;
        while (cursor.moveToNext()) {
            province = new Province();
            province.setPid(cursor.getInt(id));
            province.setName(cursor.getString(nameIndex));
            province.setProvinceID(cursor.getInt(provinceIndex));
            provinces.add(province);
        }
        return provinces;
    }
}
