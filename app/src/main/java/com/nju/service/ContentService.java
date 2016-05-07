package com.nju.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.nju.db.ContentEntry;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.util.Constant;

import java.util.ArrayList;

import model.Content;

/**
 * Created by xiaojuzhang on 2015/12/14.
 */
public class ContentService {

    private static final String TAG = ProvinceService.class.getSimpleName();
    private SQLiteDatabase db;

    public ContentService() {
        db = SchoolFriendDbHelper.newInstance().getWritableDatabase();
    }

    public synchronized void save(ArrayList<Content> contents) {
        db.beginTransaction();
        String sql = Constant.INSERT_INTO + ContentEntry.TABLE_NAME + Constant.LEFT_BACKUT + ContentEntry.COLUMN_NAME_ID + Constant.COMMA_SEP
                + ContentEntry.COLUMN_NAME_TEXT + Constant.COMMA_SEP + ContentEntry.COLUMN_NAME_IS_CONTAIN_IMAGE +
                Constant.COMMA_SEP + ContentEntry.COLUMN_NAME_USER_ID + Constant.RIGHT_BACKUT + "values(?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (Content content : contents) {
            stmt.bindLong(1, content.getId());
            stmt.bindString(2, content.getContent());
            stmt.bindLong(3, content.getIs_contain_image());
            stmt.bindLong(4, content.getUser_id());
            stmt.execute();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i(TAG, "done");
    }

    public synchronized ArrayList<Content> query() {
        ArrayList<Content> contents = new ArrayList<Content>();
        String[] projection = {
                ContentEntry.COLUMN_NAME_ID,
                ContentEntry.COLUMN_NAME_TEXT,
                ContentEntry.COLUMN_NAME_IS_CONTAIN_IMAGE,
                ContentEntry.COLUMN_NAME_USER_ID
        };
        Cursor cursor = db.query(
                ContentEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int id = cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ID);
        int textIndex = cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TEXT);
        int picNuberIndex = cursor.getColumnIndex(ContentEntry.COLUMN_NAME_IS_CONTAIN_IMAGE);
        int userIdIndex = cursor.getColumnIndex(ContentEntry.COLUMN_NAME_USER_ID);
        Content content;
        while (cursor.moveToNext()) {
            content = new Content();
            content.setId(cursor.getInt(id));
            content.setContent(cursor.getString(textIndex));
            content.setIs_contain_image(cursor.getInt(picNuberIndex));
            content.setUser_id(cursor.getInt(userIdIndex));
            contents.add(content);
        }
        return contents;
    }

    public synchronized void delete(int[] ids) {
        db.beginTransaction();
        String sql = Constant.DELETE_FROM + ContentEntry.TABLE_NAME + Constant.WHERE + ContentEntry.COLUMN_NAME_ID + "= ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        for (int id : ids) {
            stmt.bindLong(1, id);
            stmt.execute();
        }
        stmt.close();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * to delete all but the latest 200 records
     */
    private synchronized void deleteAllButLastet() {
        String sql = Constant.DELETE_FROM + ContentEntry.TABLE_NAME + Constant.WHERE + ContentEntry.COLUMN_NAME_ID
                + " not in (" +
                "SELECT " + ContentEntry.COLUMN_NAME_ID + " FROM " + ContentEntry.TABLE_NAME +
                " ORDER BY " + ContentEntry.COLUMN_NAME_DATE + " DESC " +
                "LIMIT " + Constant.MAX_SAVE_NUMBER + " )";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
        stmt.close();
    }
}
